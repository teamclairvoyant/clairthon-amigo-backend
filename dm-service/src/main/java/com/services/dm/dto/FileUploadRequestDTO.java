package com.services.dm.dto;

import lombok.*;

import java.io.InputStream;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class FileUploadRequestDTO {
    String type;
    String description;
    InputStream inputStream;
    String fileName;
    byte[] bytes;
    Long fileSize;
    String fileType;
}
