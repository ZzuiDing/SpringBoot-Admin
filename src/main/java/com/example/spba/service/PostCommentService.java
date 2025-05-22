package com.example.spba.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.dto.PostCommentDTO;
import com.example.spba.domain.entity.PostComment;

import java.util.List;

public interface PostCommentService extends IService<PostComment> {
    List<PostCommentDTO> getComments(Integer postId);
}
