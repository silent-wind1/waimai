package com.yefeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yefeng.dto.DishDto;
import com.yefeng.entity.Dish;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);
}
