package com.example.spba.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.entity.Carousel;

import java.util.List;

public interface CarouselService extends IService<Carousel> {
    List<Carousel> getAllCarousels();
}
