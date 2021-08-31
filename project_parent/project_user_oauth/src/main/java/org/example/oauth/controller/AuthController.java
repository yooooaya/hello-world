package org.example.oauth.controller;


import org.apache.commons.lang.StringUtils;
import org.example.oauth.service.AuthService;
import org.example.oauth.util.AuthToken;
import org.example.oauth.util.CookieUtil;
import org.example.pojo.Result;
import org.example.pojo.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;

    @RequestMapping("/toLogin")
    public String toLogin(@RequestParam(value = "from",required = false,defaultValue = "") String from, Model model){
        model.addAttribute("from",from);
        System.out.println(from);
        return "login";
    }


    @RequestMapping("/login")
    @ResponseBody
    public Result login(String username, String password, HttpServletResponse response){
        //校验参数
        System.out.println("login");
        try {
            //申请令牌 authtoken
            AuthToken authToken = authService.login(username, password, clientId, clientSecret);

            //将jti的值存入cookie中
            this.saveJtiToCookie(authToken.getJti(),response);
            System.out.println(response.getHeader("Set-Cookie"));
            //response.sendRedirect("http://192.168.1.46:8001/oauth/toLogin");
            //返回结果
            return new Result(true, StatusCode.OK,"登录成功",authToken.getJti());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR,"登录失败");
        }

    }

    //将令牌的断标识jti存入到cookie中
    private void saveJtiToCookie(String jti, HttpServletResponse response) {
        CookieUtil.addCookie(response,cookieDomain,"/","uid",jti,cookieMaxAge,false);
    }

    @GetMapping()
    @ResponseBody
    public Result findAll(){
        Result users = authService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",users);
    }
}
