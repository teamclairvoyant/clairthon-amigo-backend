package com.services.dm.dto;

import lombok.*;

import java.io.InputStream;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class FileUploadRequestDTO {
    private String type;
    private String description;
    private InputStream inputStream;
    private String fileName;
    private byte[] bytes;
    private Long fileSize;
    private String fileType;
}
