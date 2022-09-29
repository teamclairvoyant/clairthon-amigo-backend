package com.services.dm.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.dm.constants.Constant;
import org.springframework.util.StringUtils;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

public class Utils {

    /**
     * checking if the auth token is valid or not..
     *
     * @param token - token
     * @return - Boolean value.
     */
    public static Boolean validateToken(String token) throws Exception {

        if(Objects.isNull(token)) {
            throw new Exception(Constant.AUTH_TOKEN_ABSENT);
        }

        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(token.trim()) && token.length() > 7) {
            String jwtToken = token.substring(7);
            DecodedJWT jwt = JWT.decode(jwtToken);
            String base64EncodedBody = jwt.getPayload();
            String body = new String(Base64.getUrlDecoder().decode(base64EncodedBody));
            Map<String, Object> jsonMap = new ObjectMapper().readValue(body,
                    new TypeReference<Map<String, Object>>() {
                    });
            if (jsonMap.get("iss").toString().contains(Constant.ISSUER)
                    && Long.parseLong(jsonMap.get("exp").toString().concat("000")) > (Long)System.currentTimeMillis()) {
                return true;
            } else {
               throw new Exception(Constant.INVALID_TOKEN);
            }
        } else {
            throw new Exception(Constant.AUTH_TOKEN_ABSENT);
        }
    }

}
