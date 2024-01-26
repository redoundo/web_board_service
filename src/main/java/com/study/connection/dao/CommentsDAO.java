package com.study.connection.dao;

import com.study.connection.DBConnection;
import com.study.connection.entity.CommentsEntity;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentsDAO {

    String Sql = null;

    //select || delete sql 문을 제공하는 생성자
    public CommentsDAO (@NotNull String method , @NotNull String conditions) {
        if(method.equals("select")) {
            Sql = "SELECT comment_id , commented_content_id , comment_user , comment ,commented_date FROM comments";
            Sql = conditions.isEmpty() ? Sql + ";" : Sql + "WHERE " + conditions + ";";
        } else {
          Sql = "DELETE FROM comments WHERE " + conditions + ";";
        }
    }
    //insert sql 문을 제공하는 생성자
    public CommentsDAO(@NotNull CommentsEntity entity) {
        Sql = "INSERT INTO comments(commented_content_id , comment_user , comment ,commented_date)" +
                " VALUES(%s,'%s','%s',STR_TO_DATE('%s','%Y.%d.%m %H:%i'))";
        Sql = String.format(Sql , entity.getCommentedContentId() , entity.getCommentUser() , entity.getComment() , entity.getCommentedDate());
    }
    //update sql 문을 제공하는 생성자
    public CommentsDAO(@NotNull CommentsEntity entity , @NotNull String conditions) throws IllegalAccessException {
        List<String> value = new ArrayList<>();
        for (Field field : entity.getClass().getDeclaredFields()) {
            if(field.get(value) != null) {
                if(field.getType().getTypeName().equals(Date.class.getTypeName())) {
                    value.add(field.getName()+  "=STR_TO_DATE('" + field.get(entity) + "','%Y.%d.%m %H:%i')");
                } else if (field.getType().getTypeName().equals(String.class.getTypeName())) {
                    value.add(field.getName() + "='" + field.get(value) + "'");
                } else {
                    value.add(field.getName() + "=" + field.get(value));
                }
            }
        }
        Sql = "UPDATE comments SET " + String.join("," , value) + " WHERE " + conditions + ";";
    }

    public List<CommentsEntity> select () {
        List<CommentsEntity> comments = new ArrayList<>();
        if (Sql != null) {
            try (Connection connection = new DBConnection().getConnection()) {
                ResultSet resultSet = connection.createStatement().executeQuery(Sql);
                while (resultSet.next()) {
                    CommentsEntity entity = new CommentsEntity();
                    entity.setComment(resultSet.getString("comment"));
                    entity.setCommentId(resultSet.getInt("comment_id"));
                    entity.setCommentedDate(resultSet.getDate("commented_date"));
                    entity.setCommentUser(resultSet.getString("comment_user"));
                    entity.setCommentedContentId(resultSet.getInt("commented_content_id"));
                    comments.add(entity);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return comments;
    }


    public void update() {
        if (Sql != null) {
            try (Connection connection = new DBConnection().getConnection()) {
                int results = connection.createStatement().executeUpdate(Sql);
                System.out.println("CommentsDAO update    :   " + results);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //else { entity == null -> throw exception }
    }

}
