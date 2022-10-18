package com.yefeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.common.CustomException;
import com.yefeng.entity.Category;
import com.yefeng.entity.Dish;
import com.yefeng.entity.Setmeal;
import com.yefeng.mapper.CategoryMapper;
import com.yefeng.service.CategoryService;
import com.yefeng.service.DishService;
import com.yefeng.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        //添加查询条件，根据分类id进行查询
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.eq("category_id", id);
        //查询当前分类是否关联菜品,如果已经关联，抛出业务异常
        long count = dishService.count(wrapper);
        if(count > 0) {
            //已经关联菜品，抛出业务异常
            throw new CustomException("已经关联菜品不能删除");
        }
        //查询当前分类是否关联了套餐，如果已经关联，抛出业务异常
        QueryWrapper<Setmeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", id);
        //添加查询条件，根据分类id进行查询
        long count1 = setmealService.count(queryWrapper);
        if(count1 > 0) {
            throw new CustomException("已经关联套餐不能删除");
        }
        // 正常删除
        super.removeById(id);
    }
}
