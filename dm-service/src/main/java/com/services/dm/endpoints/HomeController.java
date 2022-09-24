package com.services.dm.endpoints;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ResponseEntity<String> home(HttpServletRequest request) {
        String serverUrl = new String(request.getRequestURL());
        String healthApi = serverUrl.concat("actuator/health");
        String swaggerApi = serverUrl.concat("swagger-ui/index.html");
        return ResponseEntity.ok("<p><a href="+healthApi+">Health page</a></p>" +
                "<p><a href="+swaggerApi+">Swagger documentation</a></p>");
    }

}
