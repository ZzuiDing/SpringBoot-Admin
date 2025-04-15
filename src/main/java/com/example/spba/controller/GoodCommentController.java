package com.example.spba.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.spba.domain.entity.GoodComment;
import com.example.spba.domain.entity.User;
import com.example.spba.service.GoodCommentService;
import com.example.spba.service.UserService;
import com.example.spba.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/goodComment")
public class GoodCommentController {

    @Autowired
    GoodCommentService goodCommentService;

    @Autowired
    UserService userService;

    @RequestMapping("/getByGoodid")
    public R getAll(@RequestParam int goodId, @RequestParam int pageNum, @RequestParam int pageSize) {
        IPage<GoodComment> goodCommentIPage = goodCommentService.selectByGoodId(goodId, pageNum, pageSize);
        if (goodCommentIPage.getRecords().isEmpty()) {
            return R.error("没有评论");
        }

        return R.success(goodCommentIPage);
    }

    @RequestMapping("/add")
    public R add(@RequestBody GoodComment goodComment) {
        goodComment.setUserId(StpUtil.getLoginIdAsInt());
        User byId = userService.getById(StpUtil.getLoginIdAsInt());
        goodComment.setUserName(byId.getName());
        goodComment.setUserAvatar(byId.getImage());

        // 正确设置时间
        goodComment.setDate(LocalDateTime.now());

        boolean result = goodCommentService.save(goodComment);
        return result ? R.success("添加成功") : R.error("添加失败");
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
