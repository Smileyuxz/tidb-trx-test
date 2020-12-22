package com.yuxz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @package: PACKAGE_NAME
 * @class: transaction.tidb.com.yuxz.TidbClient
 * @description: 执行sql语句的客户端
 * @author: yuxiuzhen
 * @date: Created in 2020/12/19 10:24 PM
 * @version: V1.0
 */
public class TidbClient {


    private Connection conn;

    public TidbClient() {

    }

    public TidbClient(Connection conn) {
        super();
        this.conn = conn;
    }

    /**
     * @param url
     * @param user
     * @param password
     * @return
     * @throws Exception
     */
    public static Connection getTidbConn(String url, String user, String password) throws Exception {
        // 加载驱动程序
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 连接数据库
        Connection conn = DriverManager.getConnection(url, user, password);
        // 关闭自动提交
        conn.setAutoCommit(false);

        return conn;

    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**
     * 执行SQL
     *
     * @param sql
     * @throws SQLException
     */
    public void executeSql(String sql) throws SQLException {
        // 创建statement用来执行SQL语句
        Statement stmt = conn.createStatement();
        // 要执行的SQL语句
        stmt.execute(sql);
        stmt.close();
    }


}
