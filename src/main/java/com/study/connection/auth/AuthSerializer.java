package com.study.connection.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.connection.dto.UsernamePasswordAuthenticationTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@RequiredArgsConstructor
public class AuthSerializer implements RedisSerializer<UsernamePasswordAuthenticationToken> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(UsernamePasswordAuthenticationToken value) throws SerializationException {
        try {
            UsernamePasswordAuthenticationTokenDto dto = new UsernamePasswordAuthenticationTokenDto();
            UserDetails details = (UserDetails)value.getPrincipal();
            dto.setUsername(details.getUsername());
            dto.setPassword(details.getPassword());
            dto.setAuthority(details.getAuthorities().stream().findFirst().get().getAuthority()
                    .replace("Optional[", "").replace("]", ""));
            dto.setName(details.getUsername());
            dto.setCredentials(value.getCredentials().toString());
            dto.setAuthenticated(value.isAuthenticated());

            return objectMapper.writeValueAsBytes(dto);
        } catch (Exception e) {
            throw new SerializationException("Error serializing object", e);
        }
    }

    @Override
    public UsernamePasswordAuthenticationToken deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            UsernamePasswordAuthenticationTokenDto token = objectMapper.readValue(bytes, UsernamePasswordAuthenticationTokenDto.class);
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(token.getAuthority());
            List<GrantedAuthority> lists = new ArrayList<>();
            lists.add(authority);
            UserDetails details = new User(token.getUsername(), token.getPassword(), lists);

            return new UsernamePasswordAuthenticationToken(details, token.getCredentials(), details.getAuthorities());
        } catch (Exception e) {
            throw new SerializationException("Error deserializing object", e);
        }
    }
}
