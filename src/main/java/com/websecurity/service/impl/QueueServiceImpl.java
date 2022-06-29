package com.websecurity.service.impl;

import com.websecurity.service.QueueService;
import com.websecurity.utils.JDBCUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

@Service
public class QueueServiceImpl implements QueueService {

    public <T> List<T> queueUser(Class<T> clazz, String sql, Object... args){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            // 1、获取连接
            connection = JDBCUtils.getConnection();
            // 预编译
            ps = connection.prepareStatement(sql);
            if (args != null){
                for (int i = 0; i < args.length; i++) {
                    ps.setObject(i + 1, args[i]);
                }
            }
            // 执行sql，获取结果集
            rs = ps.executeQuery();
            // 获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // 获取元数据的列数
            int columnCount = rsmd.getColumnCount();
            // 返回结果
            List<T> resultList = new ArrayList<>();
            while(rs.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Object object = rs.getObject(i + 1);
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, object);
                }
                resultList.add(t);
            }
            return resultList;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection, ps, rs);
        }
        return null;
    }
}
