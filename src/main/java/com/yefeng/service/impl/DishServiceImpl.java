package com.yefeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.dto.DishDto;
import com.yefeng.entity.Dish;
import com.yefeng.entity.DishFlavor;
import com.yefeng.mapper.DishMapper;
import com.yefeng.service.DishFlavorService;
import com.yefeng.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor item : flavors) {
            item.setDishId(dishId);
        }
//        List<DishFlavor> flavors = dishDto.getFlavors();
//        flavors = flavors.stream().map((item) -> {
//            item.setDishId(dishid);
//            return item;
//        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishDto.getFlavors());
        //保存菜品口味到菜品数据表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }
}
