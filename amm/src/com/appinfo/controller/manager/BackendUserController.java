package com.appinfo.controller.manager;

import com.appinfo.controller.BaseController;
import com.appinfo.pojo.BackendUser;
import com.appinfo.service.backenduser.BackendUserService;
import com.appinfo.tools.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/22 14:03
 */
@Controller
@RequestMapping("/backend")
public class BackendUserController extends BaseController {

    @Resource(name = "backendUserService")
    private BackendUserService backendUserService;

    @RequestMapping(value = "doLogin.html",method = RequestMethod.POST)
    public String doLogin(String userCode, String password, HttpSession session , HttpServletRequest request) {
        BackendUser user = null;
        try {
            user = backendUserService.login(userCode, password);
            if (user != null) {
                System.out.println(user.getUserTypeName());
                session.setAttribute(Constants.USER_SESSION, user);
                return "redirect:HOME.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("error","用户或密码错误");
        return  "backendlogin";
    }

    @RequestMapping(value = "HOME.html")
    public String home(){
        return "backend/main";
    }

}
