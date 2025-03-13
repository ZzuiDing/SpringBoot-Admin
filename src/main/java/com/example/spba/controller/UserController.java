package com.example.spba.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.spba.domain.dto.UserDTO;
import com.example.spba.domain.entity.User;
import com.example.spba.service.UserService;
import com.example.spba.utils.R;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;

@Log4j
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param request
     * @param user
     * @return
     */
    @PostMapping("/login")
    public R login(HttpServletRequest request,@RequestBody User user) {
        if (user.getName() == null || user.getPasswd() == null) {
            return R.error("用户名或密码不能为空");
        }
        if(userService.checkLogin(user).getCode()==200) {
            log.info("登录成功");
            return R.success("登录成功");
        }
        return R.error("用户名或密码错误");
    }

    /**
     * 用户注册
     * @param userDTO
     * @return
     */
    @Transactional
    @PostMapping("/register")
    public R register(@RequestBody @Validated(UserDTO.Save.class) UserDTO userDTO) {
        if(userService.register(userDTO)) {
            log.info("注册成功");
            return R.success("注册成功");
        }
        return R.error("注册失败");
    }

    @RequestMapping("/dologin")
    public String doLogin(){
        log.info("当前会话是否登录：" + StpUtil.isLogin() + StpUtil.getLoginId());
        return "当前会话是否登录：" + StpUtil.isLogin()+StpUtil.getLoginId();
    }
    @RequestMapping("/logout")
    public String logout(){
        StpUtil.logout();
        log.info("退出成功");
        return "退出成功";
    }
}
