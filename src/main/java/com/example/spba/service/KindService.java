package com.example.spba.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.entity.Kind;


public interface KindService extends IService<Kind> {

    IPage<Kind> getList(Integer pagenum, Integer pagesize,String keyword);
}
