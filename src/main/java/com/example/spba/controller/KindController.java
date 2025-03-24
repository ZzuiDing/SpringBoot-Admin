package com.example.spba.controller;

import com.example.spba.domain.entity.Kind;
import com.example.spba.service.KindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kind")
public class KindController {

    @Autowired
    KindService kindService;

    @RequestMapping("/getAll")
    public List<Kind> getAll(@RequestParam int pagenum, @RequestParam int pagesize) {
        kindService.getList(pagenum,pagesize);
        return kindService.list();
    }

    @RequestMapping("/getById")
    public Kind getById(@RequestParam Long id) {
        return kindService.getById(id);
    }

    @RequestMapping("/add")
    public boolean add(@RequestBody String name) {
        Kind kind = new Kind();
        kind.setKind(name);
        return kindService.save(kind);
    }

    @RequestMapping("/update")
    public boolean update(@RequestBody Kind kind) {
        return kindService.updateById(kind);
    }

    @RequestMapping("/delete")
    public boolean delete(@RequestParam Long id) {
        return kindService.removeById(id);
    }

    @RequestMapping("/deleteBatch")
    public boolean deleteBatch(@RequestBody List<Long> ids) {
        return kindService.removeByIds(ids);
    }

}
