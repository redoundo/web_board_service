package com.study.connection;

import com.study.connection.entity.CommentsEntity;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CommentsResults {
    //매개변수인 contentId를 문자열로도 받을 수 있으나, int가 유효성 판단에는 더 용이하다고 생각하여 integer로 정의.
    public List<CommentsEntity> GetComments(@NonNull Integer contentId) {
        List<CommentsEntity> results = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        String searchSql = "SELECT * FROM comments WHERE commented_content_id=" + contentId;
        try(Connection connection = dbConnection.getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery(searchSql);
            while(resultSet.next()) {
                CommentsEntity entity = new CommentsEntity();
                entity.setComment(resultSet.getString("comment"));
                entity.setCommentId(resultSet.getInt("comment_id"));
                entity.setCommentUser(resultSet.getString("comment_user"));
                entity.setCommentedDate(resultSet.getDate("commented_date"));
                entity.setCommentedContentId(resultSet.getInt("commented_content_id"));
                results.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return results;
    }

}
