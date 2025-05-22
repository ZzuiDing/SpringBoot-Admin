package com.example.spba.controller;


import com.example.spba.domain.entity.PostComment;
import com.example.spba.service.PostCommentService;
import com.example.spba.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/postComment")
public class PostCommentController {

    @Autowired
    private PostCommentService postCommentService;

    //新增评论
    @PostMapping("/addComment")
    public R addComment(@RequestBody PostComment postComment) {
        postCommentService.save(postComment);
        return R.success();
    }

    //获取评论
    @PostMapping("/getComments")
    public R getComments(@RequestParam Integer postId) {
        return R.success(postCommentService.getComments(postId));
    }
}
