package com.example.spba.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.spba.dao.UserMapper;
import com.example.spba.domain.dto.UserDTO;
import com.example.spba.domain.entity.User;
import com.example.spba.service.UserService;
import com.example.spba.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public R checkLogin(User user) {
        if (user.getName() == null || user.getPasswd() == null) {
            R.error("用户名或密码不能为空");
            return R.error("用户名或密码不能为空");
        }

        if(!user.getPasswd().equals(userMapper.getPasswd(user.getName()))) {
            R.error("用户名或密码错误");
            return R.error("用户名或密码错误");
        }
        user = userMapper.getUser(user.getName());
        StpUtil.login(user.getId());
//        R.success("登录成功");
        return R.success("登录成功");
    }

    @Override
    public boolean register(UserDTO userDTO) {
        if (userDTO.getName() == null || userDTO.getPasswd() == null) {
            return false;
        }
        if(userMapper.getUser(userDTO.getName()) != null) {
            return false;
        }
        User user = new User();
        user.setName(userDTO.getName());
        user.setPasswd(userDTO.getPasswd());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());
        user.setGender(userDTO.getGender());
        user.setImage(userDTO.getImage());
        return userMapper.insertUser(user) > 0;
    }
}
