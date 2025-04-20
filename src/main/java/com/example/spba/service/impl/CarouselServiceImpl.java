package com.example.spba.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.CarouseMapper;
import com.example.spba.domain.entity.Carousel;
import com.example.spba.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarouselServiceImpl extends ServiceImpl<CarouseMapper,Carousel> implements CarouselService {


    @Override
    public List<Carousel> getAllCarousels() {
        return list();
    }
}
