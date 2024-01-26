package com.study.connection.controller;

import com.study.connection.entity.FileEntity;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Part;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadFiles {

    public List<FileEntity> upload (@NotNull List<Part> files , @NotNull String contentId) throws IOException {
        List<FileEntity> lists = new ArrayList<>();

        for (Part file : files) {
            if (file.getSubmittedFileName() != null) {

                String fileName = file.getSubmittedFileName(); //파일 이름
                String fileExtension = fileName.substring(fileName.lastIndexOf(".")); // 파일 확장자
                String uploadPath =  "C:/Users/admin/file/" + fileName; // 업로드할 물리경로
                int fileSize = (int) file.getSize();


                InputStream inputStream = file.getInputStream();
                try (FileOutputStream outputStream = new FileOutputStream(uploadPath)) {
                    byte [] bytes = new byte[5 * 1024 * 1024]; //이 크기만큼만 inputStream 에서 가져온다.
                    int exist = 0;
                    //exist는 버퍼에 있는 바이트의 길이를 의미. 더이상 읽어올 것이 없을 경우 -1을 반환.
                    while ((exist = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes , 0 , exist); //bytes = 내용 , 0 = 시작지점 , exist = 남아있는 총 길이
                    }
                } catch (IOException io) {
                    System.out.println("LoadFiles.java_WhileProcessStream :   " + io.getMessage());
                }
                inputStream.close();


                FileEntity entity = FileEntity.builder()
                        .fileVolume(fileSize)
                        .filePath(uploadPath)
                        .fileOriginalName(fileName)
                        .fileName(fileName)
                        .fileExtension(fileExtension)
                        .contentIdHaveFile(Integer.parseInt(contentId))
                        .build();
                lists.add(entity);

            }
        }
        return lists;
    }

    public void download(@NotNull String path , @NotNull String name ){

    }
}