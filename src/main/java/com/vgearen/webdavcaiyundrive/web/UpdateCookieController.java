package com.vgearen.webdavcaiyundrive.web;

import com.vgearen.webdavcaiyundrive.config.Cookie;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UpdateCookieController {
    @RequestMapping(value="/update", method = RequestMethod.GET)
    public String update(HttpServletRequest request){
        String token = request.getParameter("token");
        String account = request.getParameter("account");
        String encrypt = request.getParameter("encrypt");
        String tel = request.getParameter("tel");
        if(StringUtils.hasLength(token)){
            token = token.replaceAll(" ","+");
            Cookie.setToken(token);
        }
        if(StringUtils.hasLength(account)){
            Cookie.setAccount(account);
        }
        if(StringUtils.hasLength(encrypt)){
            Cookie.setEncrypt(encrypt);
        }
        if(StringUtils.hasLength(tel)){
            Cookie.setTel(tel);
        }
        return "更新成功";
    }
}
