package com.example.spba.service;

import com.example.spba.domain.dto.UserDTO;
import com.example.spba.utils.R;

public interface UserService {
    R checkLogin(String user);

    boolean register(UserDTO userDTO);

    UserDTO getUserInfo(int userid);

    boolean updateUserInfo(UserDTO userDTO);
}
