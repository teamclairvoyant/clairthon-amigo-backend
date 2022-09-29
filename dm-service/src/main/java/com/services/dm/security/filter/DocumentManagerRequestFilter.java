package com.services.dm.security.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.dm.constants.Constant;
import com.services.dm.exceptions.RestErrorResponse;
import com.services.dm.util.Utils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Order(1)
@Component
public class DocumentManagerRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException {

        try {
            if(httpServletRequest.getRequestURI().startsWith(Constant.PATH_API_REQUESTS) &&
                    Utils.validateToken(httpServletRequest.getHeader(Constant.AUTHORIZATION))) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        } catch (Exception e) {
            RestErrorResponse errorResponse = new RestErrorResponse(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
            PrintWriter pw = httpServletResponse.getWriter();
            pw.write(convertObjectToJson(errorResponse));
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            pw.close();
        }
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
