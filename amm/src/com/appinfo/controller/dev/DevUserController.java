package com.appinfo.controller.dev;

import com.appinfo.controller.BaseController;
import com.appinfo.pojo.DevUser;
import com.appinfo.service.devuser.DevUserService;
import com.appinfo.tools.Constants;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/22 19:58
 */
@Controller
@RequestMapping("/dev")
public class DevUserController extends BaseController {
    private Logger logger = Logger.getLogger(DevUserController.class);

    @Resource
    private DevUserService devUserService;

    @RequestMapping(value = "/dologin", method = RequestMethod.POST)
    public String login(String devCode, String devPassword, HttpServletRequest request, HttpSession session) {
        DevUser devUser = null;
        try {
            devUser = devUserService.login(devCode, devPassword);
            if (devUser != null) {
                session.setAttribute("devUserSession", devUser);
                return "redirect:home.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("error", "用户名或密码错误");
        return "devlogin";
    }


    @RequestMapping(value = "home.html")
    public String home() {
        return "developer/main";
    }
}
