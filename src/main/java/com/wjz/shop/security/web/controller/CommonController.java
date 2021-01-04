package com.wjz.shop.security.web.controller;

import com.wjz.shop.security.web.dto.LoginDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CommonController {

    @RequestMapping("/403")
    public String _403(){
        return "403";
    }

    @RequestMapping("/session/invalid")
    public String session(){
        return "sessionInvalid";
    }

    public Map<String,String> login(LoginDTO dto){
        Map<String,String> map = new HashMap<>();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
//
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        boolean rememberMe = (dto.isRememberMe() == null) ? false : dto.isRememberMe();
//        String jwt = tokenProvider.createToken(authentication, rememberMe);
//
//        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);

        return map;
    }


}
