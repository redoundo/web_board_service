package com.study.connection.dao;

import com.study.connection.DBConnection;
import com.study.connection.dto.FileDto;
import com.study.connection.entity.FileEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * content_id_have_file 을 통해 select ,  insert , delete 를 진행.
 */
public class FileDao {

    public List<FileDto> select (Integer contentId) throws Exception {
        List<FileDto> list = new ArrayList<>();

        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "SELECT * FROM files WHERE content_id_have_file=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , contentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                FileDto dto = new FileDto(resultSet.getString("file_name") ,
                        resultSet.getString("file_path") , resultSet.getInt("file_id"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }

        return list;
    }

    public void insert (FileEntity entity) throws Exception {

        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "INSERT INTO files(file_name,file_path,file_extension," +
                    "file_original_name,content_id_have_file,file_volume) VALUES(?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1 , entity.getFileName());
            statement.setString(2 , entity.getFilePath());
            statement.setString(3 , entity.getFileExtension());
            statement.setString(4 , entity.getFileOriginalName());
            statement.setInt(5 , entity.getContentIdHaveFile());
            statement.setInt(6 , entity.getFileVolume());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    public void delete (Integer contentId) throws Exception {

        try (Connection connection = new DBConnection().getConnection()) {
            String sql ="DELETE FROM files WHERE content_id_have_file=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1 , contentId);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception(e);
        }

    }
}
