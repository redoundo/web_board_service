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
    @Column(name = "file_id" , nullable = false)
    private Integer fileId;
    @Column(name = "content_id_have_file" , nullable = false)
    private Integer contentIdHaveFile;
    @Column(name = "file_volume" , nullable = false)
    private Integer fileVolume;
    @Column(name = "file_name" , nullable = false)
    private String fileName;
    @Column(name = "file_original_name" , nullable = false)
    private String fileOriginalName;
    @Column(name = "file_path" , nullable = false)
    private String filePath;
    @Column(name = "file_extension" , nullable = false)
    private String fileExtension;
}
