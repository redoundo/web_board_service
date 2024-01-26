package com.study.connection.dao;

import com.study.connection.ConditionParser;
import com.study.connection.DBConnection;
import com.study.connection.dto.ContentDto;
import com.study.connection.dto.SearchOptionDto;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * contents 테이블의  select , insert ,modify , delete 기능 제공
 */
public class ContentDao {
    int total = 0;

    public List<ContentDto> select(@NotNull Map<String, String> params) {
        //board.jsp 로 호출되는 내용이었으나 select 문으로 변경.
        //TODO : form 액션 없이 jsp 에게 내용을 제공하고 페이지 이동하는게 불가능하다. webservlet 으로 해결 가능한 방법이 있는지 확인 필요.
        List<ContentDto> list = new ArrayList<>();
        ConditionParser parser = new ConditionParser();

        SearchOptionDto dto = parser.parse(params);
        String sql = parser.stringify(dto);

        try (Connection connection = new DBConnection().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, dto.fullString());
            statement.setDate(2, dto.getStart());
            statement.setDate(3, dto.getEnd());

            if (dto.getKeyword() != null || dto.getContentId() != null || dto.getCategoryId() != null) {
                if (dto.getKeyword() != null && dto.getContentId() != null && dto.getCategoryId() != null) {
                    statement.setInt(4, dto.getCategoryId());
                    statement.setInt(5, dto.getContentId());
                    statement.setString(6, dto.getKeyword());
                    statement.setInt(7 , dto.getPage());
                } else {
                    if (dto.getCategoryId() == null) {
                        if (dto.getContentId() == null) {
                            statement.setString(4, dto.getKeyword());
                            statement.setInt(5 , dto.getPage());
                        } else {
                            if (dto.getKeyword() == null) {
                                statement.setInt(4, dto.getContentId());
                                statement.setInt(5 , dto.getPage());
                            } else {
                                statement.setInt(4, dto.getContentId());
                                statement.setString(5, dto.getKeyword());
                                statement.setInt(6 , dto.getPage());
                            }
                        }
                    } else {
                        if (dto.getContentId() == null) {
                            if(dto.getKeyword() != null) {
                                statement.setInt(4, dto.getCategoryId());
                                statement.setString(5, dto.getKeyword());
                                statement.setInt(6 , dto.getPage());
                            } else{
                                statement.setInt(4, dto.getCategoryId());
                                statement.setInt(5 , dto.getPage());
                            }

                        } else {
                            statement.setInt(4, dto.getCategoryId());
                            statement.setInt(5, dto.getContentId());
                            statement.setInt(6 , dto.getPage());
                        }
                    }
                }
            } else{
                statement.setInt(4 , dto.getPage());
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ContentDto content = ContentDto.builder()
                        .content(resultSet.getString("content"))
                        .contentId(resultSet.getInt("content_id"))
                        .contentCategoryId(resultSet.getInt("content_category_id"))
                        .title(resultSet.getString("title"))
                        .nickname(resultSet.getString("nickname"))
                        .viewCount(resultSet.getInt("view_count"))
                        .password(resultSet.getString("password"))
                        .submitDate(resultSet.getDate("submit_date"))
                        .updateDate(resultSet.getDate("update_date"))
                        .fileExistence(resultSet.getBoolean("file_existence"))
                        .build();
                list.add(content);
            }

        } catch (Exception e) {
            System.out.println("ContentDao.java :   "+ e.getMessage());
            throw new RuntimeException(e);
        }
        return list;
    }

    public void insert(ContentDto dto) throws RuntimeException {
        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "INSERT INTO contents(?) VALUES(?,?,?," +
                    "?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, dto.insertString());
            statement.setInt(2, dto.getContentCategoryId());
            statement.setString(3, "'" + dto.getPassword() + "'");
            statement.setInt(4, dto.getViewCount());
            statement.setString(5, "'" + dto.getContent() + "'");
            statement.setString(6, "'" + dto.getNickname() + "'");
            statement.setString(7, "'" + dto.getTitle() + "'");
            statement.setDate(8, dto.getSubmitDate());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void modify(ContentDto dto) throws RuntimeException {
        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "UPDATE contents SET content=?,nickname=?,title=?,update_date=? WHERE content_id=?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "'" + dto.getContent() + "'");
            statement.setString(2, "'" + dto.getNickname() + "'");
            statement.setString(3, "'" + dto.getTitle() + "'");
            statement.setDate(4, Date.valueOf(LocalDateTime.now().toString()));
            statement.setInt(5, dto.getContentId());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Integer contentId) throws RuntimeException {
        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "DELETE FROM contents WHERE content_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, contentId);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int total(){
        return this.total;
    }
}
