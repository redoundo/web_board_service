package com.study.connection.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * files 테이블 엔티티
 */
@Builder
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileId" , nullable = false)
    private Integer fileId;
    @Column(name = "contentIdHaveFile" , nullable = false)
    private Integer contentIdHaveFile;
    @Column(name = "fileVolume" , nullable = false)
    private Integer fileVolume;
    @Column(name = "fileName" , nullable = false)
    private String fileName;
    @Column(name = "fileOriginalName" , nullable = false)
    private String fileOriginalName;
    @Column(name = "filePath" , nullable = false)
    private String filePath;
    @Column(name = "fileExtension" , nullable = false)
    private String fileExtension;
}
