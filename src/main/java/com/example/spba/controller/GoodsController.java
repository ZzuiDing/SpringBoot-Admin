package com.example.spba.controller;

    import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
    import com.baomidou.mybatisplus.core.metadata.IPage;
    import com.example.spba.domain.entity.Good;
    import com.example.spba.service.GoodsService;
    import com.example.spba.utils.R;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.validation.annotation.Validated;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.RestController;

    @Validated
    @RestController
    @RequestMapping("/goods")
    public class GoodsController {

        @Autowired
        private GoodsService goodsService;

        /**
         * 分页查询所有商品
         *
         * @param pageNum  当前页码，默认值为1
         * @param pageSize 每页显示数量，默认值为10
         * @return 返回分页查询结果
         */
        @RequestMapping("/ALLGoodsList")
        public R listGoods(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
            IPage<Good> goods = goodsService.getGoodList(pageNum, pageSize);
            return R.success(goods);
        }

        /**
         * 添加商品接口
         *
         * @param good 接收前端传递的商品对象
         * @return 返回添加成功提示信息
         */
        @RequestMapping("/add")
        public R addGoods(@RequestBody Good good) {
            goodsService.save(good);
            return R.success("添加商品成功");
        }

        /**
         * 删除商品接口
         *
         * @param id 根据商品ID进行删除
         * @return 返回删除成功提示信息
         */
        @RequestMapping("/delete")
        public R deleteGoods(@RequestParam Integer id) {
            goodsService.removeById(id);
            return R.success("删除商品成功");
        }

        /**
         * 更新商品接口
         *
         * @param good 商品对象包含更新的数据
         * @return 返回更新成功提示信息
         */
        @RequestMapping("/update")
        public R updateGoods(@RequestBody Good good) {
            goodsService.updateById(good);
            return R.success("更新商品成功");
        }

        /**
         * 获取商品详情接口
         *
         * @param id 商品ID用于查询
         * @return 返回查询到的商品详情信息
         */
        @RequestMapping("/detail")
        public R detailGoods(@RequestParam Integer id) {
            Good good = goodsService.getById(id);
            return R.success(good);
        }

        //TODO : 商品搜索和分类接口
        /**
         * 搜索商品接口
         *
         * @return 返回搜索页面提示信息
         */
        @RequestMapping("/search")
        public String searchGoods() {
            return "搜索商品";
        }

        //TODO : 商品分类接口
        /**
         * 商品分类接口
         *
         * @param category 根据分类名称查询
         * @return 返回分类查询提示信息
         */
        @RequestMapping("/category")
        public String categoryGoods(@RequestParam String category) {
            goodsService.list(new QueryWrapper<Good>().eq("category", category));
            return "商品分类";
        }

        @RequestMapping("/GoodListByUserId")
        public R getGoodListByUserId(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                @RequestParam Integer userId) {
            IPage<Good> goods = goodsService.getGoodListByUserId(pageNum,pageSize,userId);
            return R.success(goods);
        }
    }