package com.example.spba.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Data
@Table(name = "carousel")
public class Carousel {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String imageUrl;
    private String description; // 可选描述字段
    private Integer sortOrder; // 排序值，可用于控制轮播图显示顺序

    // Getters and Setters
}
