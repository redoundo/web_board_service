package com.study.connection.service;

import com.study.connection.dto.Notification;
import com.study.connection.entity.NotifyEntity;
import com.study.connection.mapper.NotifyMapper;
import com.study.connection.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserMapper userMapper;
    private final NotifyMapper notifyMapper;
    public void InsertNotification(UserDetails details, Notification note){
        Integer userId = userMapper.findUserIdById(details.getUsername());
        NotifyEntity entity = NotifyEntity.builder()
                .userId(userId)
                .title(note.getTitle())
                .fixOnTop(note.getFixOnTop())
                .categoryId(note.getCategoryId())
                .build();
        notifyMapper.insertNotification(entity);
    }

    public void deleteNotification(UserDetails details, Integer notifyId){
        Integer userId = userMapper.findUserIdById(details.getUsername());
        notifyMapper.deleteNotification(notifyId, userId);
    }
}
