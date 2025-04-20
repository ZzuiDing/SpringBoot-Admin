package com.example.spba.controller;

import com.example.spba.domain.entity.Carousel;
import com.example.spba.service.CarouselService;
import com.example.spba.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/carousel")
public class CarouseController {

    @Autowired
    private CarouselService carouselService;

    @GetMapping("/getAllCarousels")
    public R getAllCarousels() {
        try {
            List<Carousel> carousels = carouselService.getAllCarousels();
            return R.success(carousels);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return R.error("查询失败");
        }
    }

    @PostMapping("/add")
    public R addCarousel(@RequestBody Carousel carousel) {
        try{
            carouselService.save(carousel);
            return R.success("添加成功");
        } catch (Exception e) {
            return R.error("添加失败");
        }
    }

    @PutMapping("/update")
    public R updateCarousel(@RequestBody Carousel carousel) {
        try {
            carouselService.updateById(carousel);
            return R.success("更新成功");
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    public R deleteCarousel(@PathVariable Long id) {
        try {
            carouselService.removeById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }
}
