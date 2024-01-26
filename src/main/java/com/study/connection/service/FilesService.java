package com.study.connection.service;

import com.study.connection.dao.FileDao;
import com.study.connection.dto.FileDto;
import com.study.connection.entity.FileEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *
 */
public class FilesService {

    public List<FileDto> select (@NotNull String contentId) throws Exception{
        List<FileDto> list;

        if(!contentId.isEmpty() && !contentId.equals("null")) {
            try {
                //만일 null 이거나 잘못된 내용일 경우 controller 측에서 alert 를 보낼 수 있도록 넘긴다.
                Integer id = Integer.parseInt(contentId);
                list = new FileDao().select(id);
            }catch (Exception e) {
                throw new Exception(e);
            }
        }else {
            throw new Exception("content_id is wrong");
        }

        return list;
    }

    public void insert (@NotNull List<FileEntity> entity) throws Exception {

        //file 이 null 인지, 아니라면 반드시 필요한 인자가 null 인지 아닌지 여부 확인 후 진행.
        for (FileEntity file : entity) {
            if (file != null && file.getFilePath() != null && file.getFileName() != null &&
                    file.getContentIdHaveFile() != null && file.getFileVolume() != null && file.getFileOriginalName() != null) {

                try {
                    new FileDao().insert(file);
                } catch (Exception e) {
                    throw new Exception(e);
                }

            } else {
                throw new Exception();
            }
        }

    }

    public void delete (@NotNull String contentId) throws Exception {
        //update 를 진행하기 위해 사용된다. 매개변수로 제공된 content_id 를 가진 file 들을 전부 지운다.

        if(!contentId.isEmpty()) {
            try{
                Integer id = Integer.parseInt(contentId);
                new FileDao().delete(id);
            } catch (Exception e) {
                throw new Exception(e);
            }
        } else {
            throw new Exception();
        }
    }
}
