package com.example.spba.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostCommentDTO {
    private Long id;            // 评论 ID
    private Long postId;        // 所属文章 ID
    private String content;     // 评论内容
    private Long userId;      // 评论作者
    private Long parentId;      // 父评论 ID（根评论为 null）
    private List<PostCommentDTO> children = new ArrayList<>(); // 子评论
    private LocalDateTime createdAt;  // 评论时间

    public void addChild(PostCommentDTO child) {
        this.children.add(child);
        System.out.println("添加子评论：" + child.getId() + " -> 父评论：" + this.getId());
    }
}
