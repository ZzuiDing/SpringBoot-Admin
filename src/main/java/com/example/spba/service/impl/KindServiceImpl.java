package com.example.spba.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.domain.entity.Kind;
import com.example.spba.service.KindService;
import com.example.spba.dao.KindMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class KindServiceImpl extends ServiceImpl<KindMapper, Kind>
implements KindService{

    @Autowired
    KindMapper kindMapper;

    @Override
    public IPage<Kind> getList(Integer pagenum, Integer pagesize, String keyword) {
        Page<Kind> page = new Page<>(pagenum, pagesize);
        LambdaQueryWrapper<Kind> wrapper = new LambdaQueryWrapper<>();

        if(keyword!=null&& !keyword.trim().isEmpty()){
            wrapper.and(w ->
                    w.like(Kind::getKindName, keyword)
            );
        }
        wrapper.orderByAsc(Kind::getId);
        return kindMapper.selectPage(page, wrapper);
    }
}
