package com.example.spba.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.spba.domain.entity.Kind;
import com.example.spba.service.KindService;
import com.example.spba.utils.R;
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
    public R getAll(@RequestParam(defaultValue = "1") Integer pageNum,
                    @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<Kind> kindIPage = kindService.getList(pageNum,pageSize);
        return R.success(kindIPage);
    }

    @RequestMapping("/getById")
    public Kind getById(@RequestParam Long id) {
        return kindService.getById(id);
    }

    @RequestMapping("/add")
    public boolean add(@RequestBody Kind kind) {
        System.out.println("Received kindName: " + kind.getKindName());  // 输出接收到的 kindName
        return kindService.save(kind);  // 保存 Kind 对象
    }


    @RequestMapping("/update")
    public boolean update(@RequestBody Kind kind) {
        return kindService.updateById(kind);
    }

    @RequestMapping("/delete")
    public R delete(@RequestParam Long id) {
            kindService.removeById(id);
            return R.success("删除成功");

    }

    @RequestMapping("/deleteBatch")
    public boolean deleteBatch(@RequestBody List<Long> ids) {
        return kindService.removeByIds(ids);
    }

}
