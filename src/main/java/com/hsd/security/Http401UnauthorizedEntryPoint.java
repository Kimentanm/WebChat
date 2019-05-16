package com.hsd.security;

import com.hsd.core.Result;
import com.hsd.core.ResultCode;
import com.hsd.util.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {
    private final Logger log = LoggerFactory.getLogger(Http401UnauthorizedEntryPoint.class);

    public Http401UnauthorizedEntryPoint() {
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2) throws IOException, ServletException {
        this.log.debug("Pre-authenticated entry point called. Rejecting access");
        String returnCode = null;
        response.setContentType("application/json");
        JsonMapper jsonMapper = new JsonMapper();
        Result result = new Result();
        result.setCode(ResultCode.UNAUTHORIZED);
        String jsonString = jsonMapper.toJson(result);
        response.getWriter().print(jsonString);
        response.getWriter().flush();
    }
}
