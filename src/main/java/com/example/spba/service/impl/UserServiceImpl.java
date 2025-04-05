package com.example.spba.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.UserMapper;
import com.example.spba.domain.dto.UserDTO;
import com.example.spba.domain.entity.User;
import com.example.spba.service.UserService;
import com.example.spba.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public R checkLogin(String username) {
        User user = userMapper.getUser(username);
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
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
//        R.success("登录成功");
        log.info("登录成功"+tokenInfo);
        return R.success(tokenInfo);
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

    @Override
    public UserDTO getUserInfo(int userid) {
        User user = userMapper.selectById(userid);
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setPasswd(user.getPasswd());
        userDTO.setPhone(user.getPhone());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setGender(user.getGender());
        userDTO.setImage(user.getImage());
        return userDTO;
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        try {
            userMapper.updateUserInfo(userDTO);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
