package com.example.spba.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.example.spba.domain.dto.UserDTO;
import com.example.spba.domain.entity.User;
import com.example.spba.service.UserService;
import com.example.spba.utils.R;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@Log4j
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param loginData
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        if (username == null || password == null) {
            return R.error("用户名或密码不能为空");
        }
        if (userService.checkLogin(username).getCode() == 20000) {
            log.info("登录成功");
            return R.success(StpUtil.getTokenInfo());
        }
        return R.error("用户名或密码错误");
    }

    /**
     * 用户注册
     *
     * @param userDTO
     * @return
     */
    @Transactional
    @PostMapping("/register")
    public R register(@RequestBody @Validated(UserDTO.Save.class) UserDTO userDTO) {
        if (userService.register(userDTO)) {
            log.info("注册成功");
            return R.success("注册成功");
        }
        return R.error("注册失败");
    }

    @RequestMapping("/dologin")
    public String doLogin() {
        log.info("当前会话是否登录：" + StpUtil.isLogin() + StpUtil.getLoginId());
        return "当前会话是否登录：" + StpUtil.isLogin() + StpUtil.getLoginId();
    }

    @RequestMapping("/logout")
    public R logout() {
        StpUtil.logout();
        log.info("退出成功");
        return R.success("退出成功");
    }

    @RequestMapping("/getUserInfo/{Userid}")
    public R getUserInfo(int Userid) {
        UserDTO userDTO = userService.getUserInfo(Userid);
        return R.success(userDTO);
    }

    @RequestMapping("/updateUserInfo")
    public R updateUserInfo(@RequestBody @Validated(UserDTO.Update.class) UserDTO userDTO) {
        if (userService.updateById(userDTO)) {
            log.info("更新成功");
            return R.success("更新成功");
        }
        return R.error("更新失败");
    }

    @RequestMapping("/userInfo")
    public R userInfo(HttpServletRequest request, String token) {
        log.info(token);
//        int loginIdAsInt = StpUtil.getLoginIdAsInt();
//        Object loginIdByToken = StpUtil.getLoginIdByToken(token);
//        log.info("当前会话是否登录：" + StpUtil.isLogin() + StpUtil.getLoginId());
//        Map<String, Object> userInfo = new HashMap<>();
//        userInfo.put("id", StpUtil.getLoginIdAsInt());
        Object loginIdByToken = StpUtil.getLoginIdByToken(token);
        UserDTO userInfo1 = userService.getUserInfo(Integer.parseInt(loginIdByToken.toString()));
//        userInfo.put("token", StpUtil.getTokenValue());
        return R.success(JSON.parse(JSON.toJSONString(userInfo1)));
    }

    @RequestMapping("/getUserNames")
    public R getUserNames() {
        List<User> list = userService.list();
        String[] usernames = new String[list.size()];
//        usernames = usernames
        for (User user : list) {
            log.info(user.getName());
            usernames[list.indexOf(user)] = user.getName();
        }
        return R.success(usernames);
    }

    @RequestMapping("/getUserList")
    public R getUserList() {
        List<User> list = userService.list();
        return R.success(list);
    }

    @RequestMapping("/deleteUser")
    public R deleteUser(@RequestParam(name = "id") Integer id) {
//        int id = userId.get("id");
        if (userService.removeById(id)) {
            log.info("删除成功");
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }
}
