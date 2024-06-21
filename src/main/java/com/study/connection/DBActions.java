package com.study.connection;

import com.study.connection.entity.CommentsEntity;
import com.study.connection.entity.ContentsEntity;
import com.study.connection.entity.FilesEntity;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

public class DBActions {
    String fullContentSelect = "contentId,contentCategoryId,password,viewCount,content,nickname,title,submitDate,updateDate,(SELECT COUNT(*) FROM files WHERE files.contentIdHaveFile=contents.contentId) AS fileExistence ";
    String fullCommentSelect = "commentedContentId,commentId,commentUser,comment,commentedDate";

    List<String> fullContentSelectElements = new ArrayList<>(Arrays.asList("contentId" , "contentCategoryId" , "password" , "viewCount" , "nickname" , "title" ,
            "submitDate" , "updateDate" , "fileExistence"));

    public List<ContentsEntity> returnFullContents(String conditions) {
        List<ContentsEntity> entityList=new ArrayList<>();
        try( Connection connection = new DBConnection().getConnection() ){
            String sql = "SELECT " + this.fullContentSelect + "FROM contents";
            if(conditions != null && !conditions.isEmpty()){
                sql = sql + " WHERE " + conditions;
            }
            ResultSet resultSet = connection.createStatement().executeQuery( sql );
            while(resultSet.next()){
                ContentsEntity entity = new ContentsEntity();
                entity.setContentId(resultSet.getInt("contentId"));
                entity.setContentCategoryId(resultSet.getInt("contentCategoryId"));
                entity.setContent(resultSet.getString("content"));
                entity.setPassword(resultSet.getString("password"));
                entity.setTitle(resultSet.getString("title"));
                entity.setUpdateDate(resultSet.getDate("updateDate"));
                entity.setSubmitDate(resultSet.getDate("submitDate"));
                entity.setViewCount(resultSet.getInt("viewCount"));
                entity.setNickname(resultSet.getString("nickname"));
                entity.setFileExistence(resultSet.getBoolean("fileExistence"));
                entityList.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return entityList;
    }

    public List<Map<String,String>> conditionalSearch (String sql , List<String> selectParam ) throws RuntimeException {
        List<Map<String,String>> resultsList = new ArrayList<>();
        try(Connection connection = new DBConnection().getConnection()){
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next()) {
                Map<String,String> resultsMap = new HashMap<>();
                //매개변수 select,condition을 문자열로 보내는 대신 select의 내용을 리스트 형식으로 담은 selectParam이 요구된다.
                for(String param : selectParam == null? this.fullContentSelectElements: selectParam) {
                    //각각의 타입에 맞게 get을 진행해야하지만 짧은 코드를 위해 단순화 했다. 에러가 난다면 원래대로 변경이 필요하다.
                    resultsMap.put(param , resultSet.getString(param));
                }
                resultsList.add(resultsMap);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resultsList;
    }

    public  List<CommentsEntity> ReturnFullComments (@NonNull String contentId) {
        List<CommentsEntity> entityList = new ArrayList<>();
        String commentSql = "SELECT " + this.fullCommentSelect + " FROM comments WHERE commentedContentId=" + contentId + ";";

        try(Connection connection = new DBConnection().getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery(commentSql);
            while(resultSet.next()) {
                CommentsEntity commentsEntity = new CommentsEntity();
                commentsEntity.setCommentedContentId(resultSet.getInt("commentedContentId"));
                commentsEntity.setCommentedDate(resultSet.getDate("commentedDate"));
                commentsEntity.setCommentId(resultSet.getInt("commentId"));
                commentsEntity.setCommentUser(resultSet.getString("commentUser"));
                commentsEntity.setComment(resultSet.getString("comment"));
                entityList.add(commentsEntity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return entityList;
    }

    public void updateDB(@NonNull String sql) throws NullPointerException{
        try (Connection connection = new DBConnection().getConnection()) {
            if(!sql.isEmpty()) {
                //불필요한 변수이나 확인을 위해 임시적으로 생성한 것 : updateResult.
                Integer updateResult = connection.createStatement().executeUpdate(sql);
                System.out.println(updateResult);
            }
        } catch (Exception e){
            System.out.println("updateDB exception  :   " +  e.getMessage() +  "    sql :" + sql);
            throw new RuntimeException(e);
        }
    }


    public String aCategory(Integer id){
        String categoryName = null;
        try(Connection connection = new DBConnection().getConnection()) {
            String searchSql = "SELECT categoryName FROM categories WHERE categoryId=" + id.toString();
            ResultSet resultSet = connection.createStatement().executeQuery(searchSql);
            while(resultSet.next()) {
                categoryName = resultSet.getString("categoryName");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return categoryName;
    }

    public List<FilesEntity> existFileEntities(Integer contentId){
        List<FilesEntity> filesEntities = new ArrayList<>();
        try(Connection connection = new DBConnection().getConnection()) {
            String sql = "SELECT * FROM files WHERE contentIdHaveFile=" + contentId + ";";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next()) {
                FilesEntity file = new FilesEntity();
                file.setFileExtension(resultSet.getString("fileExtension"));
                file.setFileId(resultSet.getInt("fileId"));
                file.setFileName(resultSet.getString("fileName"));
                file.setFileVolume(resultSet.getInt("fileVolume"));
                file.setFileOriginalName(resultSet.getString("fileOriginalName"));
                file.setFilePath(resultSet.getString("filePath"));
                file.setContentIdHaveFile(resultSet.getInt("contentIdHaveFile"));
                filesEntities.add(file);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return filesEntities;
    }
}
