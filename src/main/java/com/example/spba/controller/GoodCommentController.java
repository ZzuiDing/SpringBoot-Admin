package com.example.spba.controller;

import com.example.spba.domain.entity.GoodComment;
import com.example.spba.service.GoodCommentService;
import com.example.spba.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/goodComment")
public class GoodCommentController {

    @Autowired
    GoodCommentService goodCommentService;

    @RequestMapping("/getByGoodid")
    public R getAll(@RequestParam int goodId, @RequestParam int pageNum, @RequestParam int pageSize) {
        return R.success(goodCommentService.selectByGoodId(goodId,pageNum,pageSize));
    }

    @RequestMapping("/add")
    public R add(@RequestBody GoodComment goodComment) {
        boolean result = goodCommentService.save(goodComment);
        if (result) {
            return R.success("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @RequestMapping("/del")
    public  R del(@RequestParam int id) {
        boolean result = goodCommentService.removeById(id);
        if (result) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @RequestMapping("/update")
    public R update(@RequestBody GoodComment goodComment) {
        boolean result = goodCommentService.updateById(goodComment);
        if(result){
            return R.success();
        }
        else {
            return R.error();
        }
    }

}
