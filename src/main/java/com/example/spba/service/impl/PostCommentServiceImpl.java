package com.example.spba.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.PostCommentMapper;
import com.example.spba.domain.dto.PostCommentDTO;
import com.example.spba.domain.entity.PostComment;
import com.example.spba.service.PostCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostCommentServiceImpl extends ServiceImpl<PostCommentMapper, PostComment> implements PostCommentService {

    @Autowired
    PostCommentMapper postCommentMapper;
    @Override
    public List<PostCommentDTO> getComments(Integer postId) {
        // 查询指定 postId 的所有评论，按创建时间降序排列
        QueryWrapper<PostComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        queryWrapper.orderByDesc("created_at");

        List<PostComment> postComments = postCommentMapper.selectList(queryWrapper);

        // 创建一个 Map，用于存储每个评论的 DTO
        Map<Long, PostCommentDTO> commentDTOMap = new HashMap<>();

        // 将 PostComment 转换为 PostCommentDTO，并存入 Map
        for (PostComment postComment : postComments) {
            PostCommentDTO commentDTO = new PostCommentDTO();
            commentDTO.setId(postComment.getId());
            commentDTO.setPostId(postComment.getPostId());
            commentDTO.setUserId(postComment.getUserId());
            commentDTO.setContent(postComment.getContent());
            commentDTO.setCreatedAt(postComment.getCreatedAt());
            commentDTO.setParentId(postComment.getParentId());
            commentDTOMap.put(postComment.getId(), commentDTO);
        }

        // 创建一个 List 存储顶级评论（parentId 为 null）
        List<PostCommentDTO> commentDTOList = new ArrayList<>();

        // 构建多级评论树
        for (PostComment postComment : postComments) {
            PostCommentDTO commentDTO = commentDTOMap.get(postComment.getId());

            // 如果是顶级评论，直接加入到列表
            if (postComment.getParentId() == 0) {
                commentDTOList.add(commentDTO);
            } else {
                // 如果是子评论，找到对应的父评论并添加为子评论
                PostCommentDTO parentComment = commentDTOMap.get(postComment.getParentId());
                if (parentComment != null) {
                    parentComment.addChild(commentDTO); // 添加子评论到父评论的子评论列表
                }
            }
        }

        // 返回构建好的顶级评论列表
        return commentDTOList;
    }

}
