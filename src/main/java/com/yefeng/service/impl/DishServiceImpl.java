package com.yefeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.dto.DishDto;
import com.yefeng.entity.Dish;
import com.yefeng.entity.DishFlavor;
import com.yefeng.mapper.DishMapper;
import com.yefeng.service.DishFlavorService;
import com.yefeng.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
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

    @Override
    @Transactional
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        //查询菜品口味信息
        QueryWrapper<DishFlavor> wrapper = new QueryWrapper<>();
        wrapper.eq("dish_id", dish.getId());
        List<DishFlavor> list = dishFlavorService.list(wrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //更新dish_flavor表信息delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //更新dish_flavor表信息insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().peek(item -> item.setDishId(dishDto.getId())).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }


}
