package com.example.spba.dao;

import com.example.spba.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    String getPasswd(String name);

    User getUser(String name);
}
