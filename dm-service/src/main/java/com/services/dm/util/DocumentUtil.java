package com.services.dm.util;

import com.services.dm.dto.DocumentDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DocumentUtil {

    public JSONObject convertDocumentDTOtoJSONObject(List<DocumentDTO> documents) {

        Map<String, JSONArray> map = new HashMap<>();

        documents.forEach(documentDTO -> {
            String type = documentDTO.getType();

            if (map.get(type) != null) {
                JSONArray jsonArray = map.get(type);
                jsonArray.add(documentDTO.getName());
            } else {
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(documentDTO.getName());
                map.put(type, jsonArray);
            }
        });
        return new JSONObject(map);
    }
}
