package com.services.dm.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class ResourceResponseDTO {
    String resourceType;
    String description;
    String fileName;
    Long fileSize;
    String fileType;
    String location;
}

