package com.yuxz;


import com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @package: PACKAGE_NAME
 * @class: transaction.tidb.com.yuxz.TidbTrxTestMain
 * @description:
 * @author: yuxiuzhen
 * @date: Created in 2020/12/19 10:19 PM
 * @version: V1.0
 */
public class TidbTrxTestMain {


    public static void main(String[] args) throws Exception {


        if (FileConfig.loadProperties("/tidb-datasource.properties") == null) {
            System.out.println("找不到 tidb-datasource.properties");
            return;
        }

        if (args == null || args.length == 0) {
            System.out.println("入参为空,请输入文件地址及文件名,如 /data/sql1.sql /data/sql2.sql ");
            return;
        }

        Map<Integer, String> pseudoSqlMap = new HashMap<Integer, String>();
        List<List<Integer>> sqlCombs = new ExecutionOrderCombs().permutation(args, pseudoSqlMap);

        if (sqlCombs == null || sqlCombs.isEmpty()) {
            return;
        }

        int caseNo = 1;
        for (List<Integer> sqlComb : sqlCombs) {
            new TidbTrxTestMain().transactionTestSuit(args, sqlComb, caseNo++, pseudoSqlMap);
        }

    }


    /**
     * @param sqlFiles     待测试的所有事务文件
     * @param sqlComb      sql执行顺序
     * @param testCaseNo   测试CASE编号
     * @param pseudoSqlMap 事务文件与伪sql的映射
     */
    private void transactionTestSuit(String[] sqlFiles, List<Integer> sqlComb, int testCaseNo, Map<Integer, String> pseudoSqlMap) {
        System.out.println("\n" + new Date() + "= = = = = = begin TESTCASE" + testCaseNo + " -> " + sqlComb + "   = = = = = = ");

        String url = FileConfig.getFileProperties("url", "");
        String username = FileConfig.getFileProperties("username", "");
        String password = FileConfig.getFileProperties("password", "");

        Map<Integer, TidbClient> tidbClientMap = new HashMap<Integer, TidbClient>();
        Map<Integer, BufferedReader> sqlReaderMap = new HashMap<Integer, BufferedReader>();

        try {
            if (sqlFiles.length < ExecutionOrderCombs.MIN_TX_COUNT) {
                return;
            }

            for (int sqlSeq = 0; sqlSeq < sqlFiles.length; sqlSeq++) {
                Connection connection = TidbClient.getTidbConn(url, username, password);
                tidbClientMap.put(sqlSeq, new TidbClient(connection));
                String sqlFile = pseudoSqlMap.get(sqlSeq);
                BufferedReader reader = new BufferedReader(new FileReader(sqlFile));
                sqlReaderMap.put(sqlSeq, reader);
            }

            for (int i = 0; i < sqlComb.size(); i++) {
                String sql = sqlReaderMap.get(sqlComb.get(i)).readLine();
                if (sql == null) {
                    try {
                        System.out.println(System.currentTimeMillis() + "ms - [client-" + pseudoSqlMap.get(sqlComb.get(i)) + "] execute >> " + " commit;");
                        tidbClientMap.get(sqlComb.get(i)).getConn().commit();
                    } catch (MySQLTransactionRollbackException e) {
                        System.out.println("Lock wait timeout! Case Excute Failed !");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                System.out.println(System.currentTimeMillis() + "ms - [client-" + pseudoSqlMap.get(sqlComb.get(i)) + "] execute >> " + " " + sql);
                tidbClientMap.get(sqlComb.get(i)).executeSql(sql);

            }

        } catch (MySQLTransactionRollbackException e) {
            System.out.println("Lock wait timeout! Case Excute Failed !");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 提交事务
            for (TidbClient tidbClient : tidbClientMap.values()) {
                try {
                    tidbClient.getConn().close();
                } catch (MySQLTransactionRollbackException e) {
                    System.out.println("Lock wait timeout! Case Excute Failed !");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // 关闭文件流
            for (BufferedReader sqlReader : sqlReaderMap.values()) {
                try {
                    sqlReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(new Date() + "= = = = = = end   TESTCASE" + testCaseNo + " -> " + sqlComb + "  = = = = = = " + "\n");
        }

    }


}
