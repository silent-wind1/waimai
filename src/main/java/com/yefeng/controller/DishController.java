package com.yefeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yefeng.common.R;
import com.yefeng.dto.DishDto;
import com.yefeng.entity.Category;
import com.yefeng.service.DishFlavorService;
import com.yefeng.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("dishDto={}", dishDto.toString());
        return null;
    }

}
