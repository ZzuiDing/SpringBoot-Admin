package com.example.spba.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.spba.domain.entity.Post;
import com.example.spba.service.PostService;
import com.example.spba.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public R createPost(@RequestBody Post post) {
        post.setUserId(StpUtil.getLoginIdAsLong());
        postService.save(post);
        return R.success("Post created successfully");
    }

    @PostMapping("/delete")
    public R deletePost(@RequestBody Long postId) {
        Post post = postService.getById(postId);
        if (post == null) {
            return R.error("Post not found");
        }
        if (!post.getUserId().equals(StpUtil.getLoginIdAsLong())) {
            return R.error("You do not have permission to delete this post");
        }
        postService.removeById(postId);
        return R.success("Post deleted successfully");
    }

    //随机获取10条帖子
    @PostMapping("/random")
    public R getRandomPosts() {
        List<Post> posts = postService.list(new QueryWrapper<Post>().orderByDesc("RAND()").last("LIMIT 10"));
        return R.success(posts);
    }
}
