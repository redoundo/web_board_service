package com.study.connection.dao;

import com.study.connection.DBConnection;
import com.study.connection.entity.FilesEntity;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilesDAO {
    String Sql = null;
    
    // select || delete sql 문을 제공하는 생성자
    public FilesDAO(@NotNull String method , @NotNull String conditions) {
        if(method.equals("select")) {
            Sql = "SELECT file_name , file_path , file_extension , file_original_name , content_id_have_file, file_volume FROM files ";
            Sql = conditions.isEmpty() ? Sql + ";" : Sql + "WHERE " + conditions + ";";
        } else {
            Sql = "DELETE FROM files WHERE " + conditions + ";";
        }
    }
    //insert sql 문을 제공하는 생성자
    public FilesDAO(@NotNull FilesEntity entity) {
        Sql = "INSERT INTO files( file_name , file_path , file_extension , file_original_name , content_id_have_file, file_volume ) " +
                "VALUES('%s','%s','%s','%s',%s,%s);";
        Sql = String.format(Sql , entity.getFileName() , entity.getFilePath() , entity.getFileExtension() , entity.getFileOriginalName() , entity.getContentIdHaveFile() , entity.getFileVolume());
    }
    //update sql 문을 제공하는 생성자
    public FilesDAO (@NotNull FilesEntity entity , @NotNull String conditions) throws IllegalAccessException {
        List<String> values = new ArrayList<>();
        for (Field field : entity.getClass().getDeclaredFields()) {
            if(field.get(values) != null) {
                if(Objects.equals(field.get(entity) , String.class.getName())) {
                    values.add(field.getName() + "='" + field.get(entity) + "'");
                } else {
                    values.add(field.getName() + "=" + field.get(entity));
                }
            }
        }
        Sql = String.join("," , values) + "WHERE " + conditions;
    }
    
    public List<FilesEntity> select () {
        List<FilesEntity> files = new ArrayList<>();
        if(Sql != null) {
            try(Connection connection = new DBConnection().getConnection()) {
                ResultSet resultSet = connection.createStatement().executeQuery(Sql);
                while (resultSet.next()) {
                    FilesEntity file = new FilesEntity();
                    file.setFileExtension(resultSet.getString("file_extension"));
                    file.setFileId(resultSet.getInt("file_id"));
                    file.setFileName(resultSet.getString("file_name"));
                    file.setFilePath(resultSet.getString("file_path"));
                    file.setFileVolume(resultSet.getInt("file_volume"));
                    file.setContentIdHaveFile(resultSet.getInt("content_id_have_file"));
                    file.setFileOriginalName(resultSet.getString("file_original_name"));
                    files.add(file);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return files;
    }

    public void update () {
        if(Sql != null) {
            try(Connection connection = new DBConnection().getConnection()) {
                int value = connection.createStatement().executeUpdate(Sql);
                System.out.println("FilesDAOUpdateValue :    " + value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
