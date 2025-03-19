package com.example.spba.controller;

import com.example.spba.domain.entity.Kind;
import com.example.spba.service.KindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kind")
public class KindController {

    @Autowired
    KindService kindService;

    @RequestMapping("/getAll")
    public List<Kind> getAll() {
        return kindService.list();
    }

    @RequestMapping("/getById")
    public Kind getById(Long id) {
        return kindService.getById(id);
    }

    @RequestMapping("/add")
    public boolean add(Kind kind) {
        return kindService.save(kind);
    }

    @RequestMapping("/update")
    public boolean update(Kind kind) {
        return kindService.updateById(kind);
    }

    @RequestMapping("/delete")
    public boolean delete(Long id) {
        return kindService.removeById(id);
    }

    @RequestMapping("/deleteBatch")
    public boolean deleteBatch(List<Long> ids) {
        return kindService.removeByIds(ids);
    }

}
