package com.study.connection.dao;

import com.study.connection.DBConnection;
import com.study.connection.entity.ContentsEntity;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContentsDAO {
    String Sql = null;

    public ContentsDAO (){}
    //select || delete sql 문 제공
    public ContentsDAO(@NotNull String method, @NotNull String conditions) {
        if(method.equals("select")) {
            Sql = "SELECT content_id,content_category_id,password,view_count,content,nickname,title," +
                    "submit_date,update_date,(SELECT COUNT(*) FROM files WHERE files.content_id_have_file=contents.content_id) AS file_existence " +
                    " FROM contents ";
            Sql = conditions.isEmpty() ? Sql + ";" : Sql + "WHERE " + conditions + ";";
        } else {
            Sql = "DELETE FROM contents WHERE " + conditions;
        }
    }
    //limit 가 추가된 select sql 문
    public ContentsDAO(@NotNull Integer page , @NotNull String conditions) {
        Sql = "SELECT content_id,content_category_id,password,view_count,content,nickname,title," +
                "submit_date,update_date,(SELECT COUNT(*) FROM files WHERE files.content_id_have_file=contents.content_id) AS file_existence " +
                " FROM contents ";
        Sql = conditions.isEmpty() ? Sql + " limit %d,10;" : Sql + "WHERE " + conditions + " limit %d,10;";
        Sql = String.format(Sql , page);
    }

    // insert sql 문 생성
    public ContentsDAO ( @NotNull ContentsEntity values ) {
        Sql = "INSERT INTO contents(content_category_id,password,view_count,content,nickname,title,submit_date) VALUES(%s,'%s',0,'%s','%s','%s',STR_TO_DATE('%s','%Y.%d.%m %H:%i'));";
        Sql = String.format(Sql, values.getContentCategoryId() , values.getPassword() , values.getContent() , values.getNickname() , values.getTitle() , values.getSubmitDate());
    }
    //update sql 문 생성
    public ContentsDAO (@NotNull ContentsEntity values , @NotNull String conditions) throws IllegalAccessException {
        List<String> val = new ArrayList<>();
        for(Field field : values.getClass().getDeclaredFields()) {
            if(field.get(values) != null){
                if(field.getType().getName().equals(Date.class.getTypeName())) {
                    val.add(field.getName() + "=STR_TO_DATE('" + field.get(values) + "','%Y.%d.%m %H:%i'");
                } else if (field.getType().getName().equals(String.class.getTypeName())) {
                    val.add(field.getName() + "='" + field.get(values) + "'");
                } else {
                    val.add(field.getName() + "=" + field.get(values));
                }
            }
        }
        Sql = "UPDATE contents SET " + String.join("," ,val) + "WHERE " +conditions + ";";
    }
    // 검색 조건을 포함 여부를 확인하고 해당 내용의 전체 개수를 구한다.
    public  int total (String condition) {
        int number = 0;
        try(Connection connection = new DBConnection().getConnection()) {
            String sql = "SELECT COUNT(*) AS count FROM contents";
            sql = condition != null ? sql + "WHERE " + condition + ";" : sql + ";";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                number = resultSet.getInt("count");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return number;
    }

    public List<ContentsEntity> select() {
        List<ContentsEntity> results = new ArrayList<>();
        if (Sql != null) {
            try (Connection connection = new DBConnection().getConnection()) {
                ResultSet resultSet = connection.createStatement().executeQuery(Sql);
                while (resultSet.next()) {
                    ContentsEntity entity = new ContentsEntity();
                    entity.setContentId(resultSet.getInt("content_id"));
                    entity.setContentCategoryId(resultSet.getInt("content_category_id"));
                    entity.setContent(resultSet.getString("content"));
                    entity.setPassword(resultSet.getString("password"));
                    entity.setTitle(resultSet.getString("title"));
                    entity.setUpdateDate(resultSet.getDate("update_date"));
                    entity.setSubmitDate(resultSet.getDate("submit_date"));
                    entity.setViewCount(resultSet.getInt("view_count"));
                    entity.setFileExistence(resultSet.getBoolean("file_existence"));
                    results.add(entity);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return results;
    }

    public void update() {
        if (Sql != null) {
            try (Connection connection = new DBConnection().getConnection()) {
                int results = connection.createStatement().executeUpdate(Sql);
                System.out.println("ContentDAO update    :   " + results);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //else { entity == null -> throw exception }
    }
}
