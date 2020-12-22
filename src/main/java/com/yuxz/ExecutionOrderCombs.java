package com.yuxz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @package: PACKAGE_NAME
 * @class: transaction.tidb.com.yuxz.ExecutionOrderCombs
 * @description: 事务测试
 * @author: yuxiuzhen
 * @date: Created in 2020/12/19 9:12 PM
 * @version: V1.0
 */
public class ExecutionOrderCombs {

    /**
     * 最少事务数
     */
    final static int MIN_TX_COUNT = 1;
    private boolean[] sqlUsed;
    /**
     * 用HashSet接收结果,剔除重复的排列结果,可以减少重复case
     */
    private List<List<Integer>> executionOrderCombs;
    private int allSqlCount;


    public List<List<Integer>> permutation(String[] sqlFiles, Map<Integer, String> pseudoSqlMap) {
        if (sqlFiles.length < MIN_TX_COUNT) {
            return null;
        }

        boolean fileCheckExceptionFlag = CommonUtil.checkFileExist(sqlFiles);
        if (fileCheckExceptionFlag) {
            return null;
        }

        List<Integer> permutationSqlList = new ArrayList<Integer>();
        for (int sqlSeq = 0; sqlSeq < sqlFiles.length; sqlSeq++) {
            // 多加一个模拟commit的行数
            int sqlCount = CommonUtil.getFileLineNum(sqlFiles[sqlSeq]) + 1;
            allSqlCount += sqlCount;
            System.out.println(sqlFiles[sqlSeq] + "文件执行的伪代码为:" + sqlSeq);
            pseudoSqlMap.put(sqlSeq, sqlFiles[sqlSeq]);
            // 用一个字符表示一个文件的sql-组合排列顺序
            List pseudoSql = CommonUtil.generatePseudoSqls(sqlCount, sqlSeq);
            permutationSqlList.addAll(pseudoSql);
        }

        List<Integer> perm = new ArrayList<Integer>();
        sqlUsed = new boolean[allSqlCount];
        executionOrderCombs = new ArrayList<List<Integer>>();

        backtrack(permutationSqlList, executionOrderCombs, 0, perm);

        System.out.println("所有SQL执行顺序的" + executionOrderCombs.size() + "组合情况: " + executionOrderCombs);

        return executionOrderCombs;
    }

    /**
     * 递归 考虑有重复元素排列
     *
     * @param permutationSqls
     * @param executionOrderCombs
     * @param idx
     * @param permutationSql
     */
    public void backtrack(List<Integer> permutationSqls, List<List<Integer>> executionOrderCombs, int idx, List<Integer> permutationSql) {

        // end 长度一致时结束
        if (idx == permutationSqls.size()) {
            executionOrderCombs.add(new ArrayList<Integer>(permutationSql));
            return;
        }
        for (int i = 0; i < permutationSqls.size(); i++) {
            // 树层上去重
            if (i > 0 && permutationSqls.get(i).equals(permutationSqls.get(i - 1)) && !sqlUsed[i - 1]) {
                continue;
            }
            if (sqlUsed[i]) {
                continue;
            }
            permutationSql.add(permutationSqls.get(i));
            sqlUsed[i] = true;
            backtrack(permutationSqls, executionOrderCombs, idx + 1, permutationSql);
            sqlUsed[i] = false;
            permutationSql.remove(idx);
        }

    }


}
