package com.example.spba.service;

import com.example.spba.domain.entity.User;
import com.example.spba.utils.R;

public interface UserService {
    R checkLogin(User user);
}
