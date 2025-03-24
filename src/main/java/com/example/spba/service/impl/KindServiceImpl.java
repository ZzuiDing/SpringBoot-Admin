package com.example.spba.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.domain.entity.Kind;
import com.example.spba.service.KindService;
import com.example.spba.dao.KindMapper;
import org.springframework.stereotype.Service;

/**
* @author Zui_Ding
* @description 针对表【kind(商品种类)】的数据库操作Service实现
* @createDate 2025-03-19 19:42:51
*/
@Service
public class KindServiceImpl extends ServiceImpl<KindMapper, Kind>
implements KindService{

    @Override
    public void getList(int pagenum, int pagesize) {

    }
}
