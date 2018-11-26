package com.appinfo.controller;

import com.appinfo.tools.Constants;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/22 14:06
 * 用户登录控制类
 */
@Controller
public class LoginController extends BaseController {
    private Logger logger = Logger.getLogger(LoginController.class);

    @RequestMapping(value = "/manager/login")
    public String backendLogin(){
        logger.debug("管理员登陆=====================>");
        return "backendlogin";
    }

    @RequestMapping(value = "/dev/login")
    public String devLogin(){
        logger.debug("开发者登陆=====================>");
        return "devlogin";
    }

    @RequestMapping(value = "/dev/logout")
    public String devLogout(HttpSession session){
        logger.debug("开发者注销=====================>");
        session.removeAttribute(Constants.DEV_USER_SESSION);
        return "devlogin";
    }
}
