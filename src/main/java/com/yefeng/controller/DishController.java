package com.yefeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yefeng.common.R;
import com.yefeng.dto.DishDto;
import com.yefeng.entity.Category;
import com.yefeng.entity.Dish;
import com.yefeng.service.CategoryService;
import com.yefeng.service.DishFlavorService;
import com.yefeng.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("dishDto={}", dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("添加分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.like(name != null, "name", name);
        wrapper.orderByDesc("update_time");
        dishService.page(dishPage, wrapper);
        // 将dishPage中的属性值拷贝到dishDtoPage忽略Page类中的records
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        // 获取dishPage中的Records
        List<Dish> records = dishPage.getRecords();
        // dishPage中的records是缺少
        List<DishDto> list = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            //根据id查分类对象
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

}
