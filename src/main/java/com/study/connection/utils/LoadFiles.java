package com.study.connection.utils;

import com.study.connection.dto.file.FilePartDto;
import com.study.connection.entity.FileEntity;
import com.study.connection.error.CustomRuntimeException;
import com.study.connection.error.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.study.connection.utils.CheckValid.checking;

/**
 * file upload , download 기능.
 */
public class LoadFiles {

    @Value("${spring.servlet.multipart.location}")
    private String uploadUrl;

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
                    String uploadPath =  uploadUrl + fileName; // 업로드할 물리경로
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

    /**
     * 업로드를 진행하고 파일에 담긴 정보를 추출하여 fileEntity 에 담아 반환한다.
     * @param files null 이 아니고 하나라도 존재하는 multiPartFile 들
     * @param contentId files 테이블의 contentIdHaveFile 을 위해 필요한 내용.
     * @return files 테이블에 생성해야하는 내용.
     */
    public List<FileEntity> upload(@NotNull List<MultipartFile> files , @NotNull Integer contentId) {

        List<FileEntity> list = new ArrayList<>();
        for (MultipartFile file : files) {

            if (file.getOriginalFilename() != null) {

                String fileName = file.getName(); //파일 이름
                String originalName = file.getOriginalFilename();
                String fileExtension = fileName.substring(fileName.lastIndexOf(".")); // 파일 확장자
                String uploadPath =  uploadUrl + fileName + fileExtension; // 업로드할 물리경로
                int fileSize = (int) file.getSize();

                FileEntity entity = null;
                try (FileOutputStream outputStream = new FileOutputStream(uploadPath)) {
                    InputStream inputStream = file.getInputStream();
                    byte [] bytes = new byte[5 * 1024 * 1024]; //이 크기만큼만 inputStream 에서 가져온다.
                    int exist = 0;
                    //exist 는 버퍼에 있는 바이트의 길이를 의미. 더이상 읽어올 것이 없을 경우 -1을 반환.
                    while ((exist = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes , 0 , exist); //bytes = 내용 , 0 = 시작지점 , exist = 남아있는 총 길이
                    }
                    inputStream.close();
                    entity = FileEntity.builder()
                            .fileVolume(fileSize)
                            .filePath(uploadPath)
                            .fileOriginalName(originalName)
                            .fileName(fileName)
                            .fileExtension(fileExtension)
                            .contentIdHaveFile(contentId)
                            .build();

                    list.add(entity);

                } catch (IOException e){
                    if (entity != null && entity.getContentIdHaveFile() != null) {
                        // 거의 존재하지 않겠지만 entity 는 만들어졌지만 모종의 이유로 예외가 발생했을 때 , list 에 넣는다.
                        list.add(entity);
                    }
                    break;// 지금 것 아니면 전에 처리된 것이라도 반환하기 위해 break;
                }
            }
        }
        return list;
    }




    /**
     * 파일 경로와 해당 경로로 파일이 존재하는지 여부를 확인한 뒤에 inputStreamResource 을 생성해 반환한다.
     * @param path 파일 경로.
     * @return inputStreamResource 반환
     */
    public InputStreamResource download(String path){
        InputStreamResource resource;
        try{
            if(checking.checkString(path)){
                File file = new File(path);
                if(file.exists()){
                    resource = new InputStreamResource(new FileInputStream(file));
                } else{
                    throw new CustomRuntimeException(ErrorCode.CANT_FIND_FILE_ERROR);
                }
            } else {
                throw new CustomRuntimeException(ErrorCode.INVALID_STRING_ARGUMENT);
            }
        }catch (FileNotFoundException e){
            throw new CustomRuntimeException(ErrorCode.CANT_FIND_FILE_ERROR);
        }
        return resource;
    }

    public Boolean deleteFile(@NotNull String filePath){
        boolean done;
        File file = new File(filePath);
        if(file.exists()){
            done = file.delete();
        }else{
            done = true;
        }
        return done;
    }
}
