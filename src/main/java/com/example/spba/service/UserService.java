package com.example.spba.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.dto.UserDTO;
import com.example.spba.domain.entity.User;
import com.example.spba.utils.R;

import javax.validation.constraints.NotBlank;

public interface UserService extends IService<User> {
    R checkLogin(String user);

    boolean register(UserDTO userDTO);

    UserDTO getUserInfo(int userid);

    boolean updateUserInfo(UserDTO userDTO);

    User getByname(String name);
}
