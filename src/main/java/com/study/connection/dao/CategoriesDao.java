package com.study.connection.dao;

import com.study.connection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class CategoriesDao {
    public Map<Integer ,String> select() {
        Map<Integer , String> map = new HashMap<>();
        try(Connection connection = new DBConnection().getConnection()) {
            String sql = "SELECT category_id , category_name FROM categories";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                map.put(resultSet.getInt("category_id")
                        , resultSet.getString("category_name"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
