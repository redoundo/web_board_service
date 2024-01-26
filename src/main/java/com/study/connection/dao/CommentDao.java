package com.study.connection.dao;

import com.study.connection.DBConnection;
import com.study.connection.entity.CommentEntity;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {
    public List<CommentEntity> select(Integer contentId) throws Exception{
        List<CommentEntity> list = new ArrayList<>();

        try(Connection connection = new DBConnection().getConnection()) {
            String sql = "SELECT * FROM comments WHERE commented_content_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , contentId);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                CommentEntity entity = CommentEntity.builder()
                        .comment(resultSet.getString("comment"))
                        .commentId(resultSet.getInt("comment_id"))
                        .commentedContentId(resultSet.getInt("commented_content_id"))
                        .commentedDate(resultSet.getDate("commented_date"))
                        .commentUser(resultSet.getString("comment_user"))
                        .build();
                list.add(entity);
            }
        }
        return list;
    }

    public void insert (@NotNull CommentEntity entity) throws Exception {

        try(Connection connection = new DBConnection().getConnection()) {
            String sql = "INSERT INTO comments(commented_content_id,comment,comment_user," +
                    "commented_date) VALUES(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , entity.getCommentedContentId());
            statement.setString(2 , entity.getComment());
            statement.setString(3 , entity.getCommentUser());
            statement.setDate(4 , entity.getCommentedDate());
            statement.executeUpdate();
        }

    }
    public void delete (Integer commentId) throws Exception {

        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "DELETE FROM comments WHERE comment_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , commentId);
            statement.executeUpdate();
        }
    }
}
