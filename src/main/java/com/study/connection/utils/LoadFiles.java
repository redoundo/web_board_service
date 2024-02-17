package com.study.connection.utils;

import com.study.connection.dto.FilePartDto;
import com.study.connection.entity.FileEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일 업로드 기능 제공.
 */
public class LoadFiles {
    /**
     *
     * @param files
     * @param contentId
     * @return
     * @throws IOException
     */
    public List<FileEntity> upload(@NotNull FilePartDto files, @NotNull String contentId) throws IOException {
        List<FileEntity> list = new ArrayList<>();
        List<MultipartFile> parts = new ArrayList<>();

        if(files.getFile1() != null){parts.add(files.getFile1());}
        if(files.getFile2() != null){parts.add(files.getFile2());}
        if(files.getFile3() != null){parts.add(files.getFile3());}

        if(!parts.isEmpty()){
            for (MultipartFile file : parts) {
                if (file.getOriginalFilename() != null) {

                    String fileName = file.getName(); //파일 이름
                    String originalName = file.getOriginalFilename();
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
                            .fileOriginalName(originalName)
                            .fileName(fileName)
                            .fileExtension(fileExtension)
                            .contentIdHaveFile(Integer.parseInt(contentId))
                            .build();
                    list.add(entity);

                }
            }
        }
        return list;
    }
    public void download(@NotNull String path , @NotNull String name ){

    }
}
