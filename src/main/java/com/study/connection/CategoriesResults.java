package com.study.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

//별도의 조건이 없는 카테고리 결과만 반환.
public class CategoriesResults {
    public Map<Integer,String> categoriesSearch(){
        Map<Integer,String> categories = new HashMap<>();
        try(Connection connection = new DBConnection().getConnection()) {
            String searchSql = "SELECT categoryId,categoryName FROM categories";
            ResultSet resultSet = connection.createStatement().executeQuery(searchSql);
            while(resultSet.next()) {
                categories.put(resultSet.getInt("categoryId"),resultSet.getString("categoryName"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

}