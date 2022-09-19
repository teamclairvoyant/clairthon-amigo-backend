package com.services.dm.dto;

import lombok.*;
import org.springframework.core.io.ByteArrayResource;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class FileDownloadDTO {
    private ByteArrayResource resource;
    private String name;
    private String mimeType;
}
