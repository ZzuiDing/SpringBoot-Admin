package com.example.spba.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @RequestMapping("/getUserInfo")
    public R getUserInfo() {
        int loginIdAsInt = StpUtil.getLoginIdAsInt();
        UserDTO userDTO = userService.getUserInfo(loginIdAsInt);
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
    public R userInfo(HttpServletRequest request) {
//        log.info(token);
//        int loginIdAsInt = StpUtil.getLoginIdAsInt();
//        Object loginIdByToken = StpUtil.getLoginIdByToken(token);
//        log.info("当前会话是否登录：" + StpUtil.isLogin() + StpUtil.getLoginId());
//        Map<String, Object> userInfo = new HashMap<>();
//        userInfo.put("id", StpUtil.getLoginIdAsInt());
//        Object loginIdByToken = StpUtil.getLoginIdByToken(token);
        log.info("当前会话是否登录：" + StpUtil.isLogin() + StpUtil.getLoginId());
        int loginId = StpUtil.getLoginIdAsInt();
        UserDTO userInfo1 = userService.getUserInfo(loginId);
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
    public R getUserList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(required = false, defaultValue = "") String keyword) {
        // 创建分页对象
        Page<User> userPage = new Page<>(page, pageSize);

        // 创建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 如果有关键字，则添加模糊查询条件
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.and(w ->
                    w.like("name", keyword)
                            .or()
                            .like("email", keyword)
                            .or()
                            .like("phone", keyword)
            );
        }
        // 添加排序条件
        queryWrapper.orderByDesc("id");
        // 执行查询
        // 查询分页数据
        IPage<User> resultPage = userService.page(userPage, queryWrapper);

        // 返回分页数据
        return R.success(resultPage);
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
