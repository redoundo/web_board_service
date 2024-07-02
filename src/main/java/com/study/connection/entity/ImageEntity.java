package com.study.connection.entity;

import jakarta.annotation.Nullable;
import lombok.*;

/**
 * 파일 업로드 내용.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ImageEntity {
    private String imageName;
    private String imageOriginalName;
    private Integer imageVolume;
    private String imagePath;
    private Integer postId;
    private Integer postTable;
    private String imageExtension;
    @Nullable private Integer imageId;
}
