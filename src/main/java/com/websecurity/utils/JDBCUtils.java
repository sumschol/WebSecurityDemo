package com.websecurity.utils;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 功能描述：操作数据库的工具类
 * @author leo-zu
 * @date 2022-01-10
 *
 */
public class JDBCUtils {

    /**
     * 功能描述：获取数据库连接
     * @return 连接
     * @throws Exception
     */
    public static Connection getConnection() throws Exception{
        // 1、加载配置文件
        InputStream inputStream = JDBCUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pro = new Properties();
        pro.load(inputStream);
        String driverClass = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/dbtest1?serverTimezone=GMT%2B8";
        String user = "root";
        String password = "root";

        // 2、加载驱动
        Class.forName(driverClass);

        // 3、获取连接
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * 功能描述：关闭资源
     * @param conn
     * @param ps
     */
    public static void closeResource(Connection conn, Statement ps){
        try {
            if(ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述：关闭资源
     * @param conn
     * @param ps
     * @param rs
     */
    public static void closeResource(Connection conn, Statement ps, ResultSet rs){
        try {
            if(ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}