package com.services.dm.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class ResourceResponseDTO {
    private String resourceType;
    private String description;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private String location;
}

