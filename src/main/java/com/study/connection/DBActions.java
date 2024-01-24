package com.study.connection;

import com.study.connection.entity.CommentsEntity;
import com.study.connection.entity.ContentsEntity;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

public class DBActions {
    String fullContentSelect = "content_id,content_category_id,password,view_count,nickname,title,submit_date,update_date,(SELECT COUNT(*) FROM files WHERE files.content_id_have_file=contents.content_id) AS file_existence ";
    String fullCommentSelect = "commented_content_id,comment_id,comment_user,comment,commented_date";

    List<String> fullContentSelectElements = new ArrayList<>(Arrays.asList("content_id" , "content_category_id" , "password" , "view_count" , "nickname" , "title" ,
            "submit_date" , "update_date" , "file_existence"));

    public List<ContentsEntity> returnFullContents(String conditions) {
        DBConnection dbConnection = new DBConnection();
        List<ContentsEntity> entityList=new ArrayList<>();
        try( Connection connection = dbConnection.getConnection() ){
            String sql = "SELECT " + fullContentSelect + "FROM contents";
            if(conditions != null && !conditions.isEmpty()){
                sql = sql + " WHERE " + conditions;
            }
            ResultSet resultSet = connection.createStatement().executeQuery( sql );
            while(resultSet.next()){
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
                entityList.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return entityList;
    }

    public List<Map<String,String>> conditionalSearch (String select , @NonNull String condition , List<String> selectParam ) throws RuntimeException {
        DBConnection conditionalSearch = new DBConnection();
        List<Map<String,String>> resultsList = new ArrayList<>();
        String searchSql = "SELECT ";
        if(select == null || selectParam == null){
            //편의성을 위해 제공했지만 전체 내용을 검색하게 된다.
            searchSql = fullContentSelect;
        }else {
            searchSql = searchSql+ select + "FROM contents ";
        }
        if(condition.length()>1) {
            searchSql = searchSql + "WHERE " + condition + ";";
        }else {
            searchSql = searchSql + ";";
        }
        try(Connection connection = conditionalSearch.getConnection()){
            ResultSet resultSet = connection.createStatement().executeQuery(searchSql);
            while(resultSet.next()) {
                Map<String,String> resultsMap = new HashMap<>();
                //매개변수 select,condition을 문자열로 보내는 대신 select의 내용을 리스트 형식으로 담은 selectParam이 요구된다.
                for(String param : selectParam == null? fullContentSelectElements: selectParam) {
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

    public  List<CommentsEntity> ReturnFullComments (@NonNull String content_id) {
        List<CommentsEntity> entityList = new ArrayList<>();
        String commentSql = "SELECT " + fullCommentSelect + " FROM WHERE content_id=" + content_id + ";";

        DBConnection dbConnection = new DBConnection();
        try(Connection connection = dbConnection.getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery(commentSql);
            while(resultSet.next()) {
                CommentsEntity commentsEntity = new CommentsEntity();
                commentsEntity.setCommentedContentId(resultSet.getInt("comment_content_id"));
                commentsEntity.setCommentedDate(resultSet.getDate("commented_date"));
                commentsEntity.setCommentId(resultSet.getInt("comment_id"));
                commentsEntity.setCommentUser(resultSet.getString("comment_user"));
                commentsEntity.setComment(resultSet.getString("comment"));
                entityList.add(commentsEntity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return entityList;
    }

    public void updateDB(@NonNull String sql) throws NullPointerException{
        DBConnection dbConnection = new DBConnection();
        try (Connection connection = dbConnection.getConnection()) {
            if(!sql.isEmpty()) {
                //불필요한 변수이나 확인을 위해 임시적으로 생성한 것 : updateResult.
                Integer updateResult = connection.createStatement().executeUpdate(sql);
                System.out.println(updateResult);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
