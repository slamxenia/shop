package com.wjz.shop.security.handler;

import com.wjz.shop.security.common.utils.ControllerTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        boolean isAjax = ControllerTools.isAjaxRequest(request);
        System.out.println("CustomAccessDeniedHandler handle");
        if (!response.isCommitted()) {
            if (isAjax) {
                String msg = e.getMessage();
                log.info("accessDeniedException.message:" + msg);
                String accessDenyMsg = "{\"code\":\"403\",\"msg\":\"no permission\"}";
                ControllerTools.print(response, accessDenyMsg);
            } else {
                request.setAttribute(WebAttributes.ACCESS_DENIED_403, e);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                RequestDispatcher dispatcher = request.getRequestDispatcher("/403");
                dispatcher.forward(request, response);
            }
        }
    }
}
