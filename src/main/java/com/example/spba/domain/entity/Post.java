package com.example.spba.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("post")
public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String imagesJson;
    private LocalDateTime createdAt;
}

