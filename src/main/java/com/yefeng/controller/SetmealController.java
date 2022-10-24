package com.yefeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yefeng.common.R;
import com.yefeng.dto.SetmealDto;
import com.yefeng.entity.Setmeal;
import com.yefeng.entity.SetmealDish;
import com.yefeng.service.SetmealDishService;
import com.yefeng.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("setmealDto={}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDishPage = new Page<>();
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Setmeal::getName, name);
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, wrapper);
        BeanUtils.copyProperties(pageInfo, setmealDishPage);
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Setmeal setmeal = setmealService.getById(categoryId);
            if (setmeal != null) {
                String setmealName = setmeal.getName();
                setmealDto.setCategoryName(setmealName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDishPage.setRecords(list);
        return R.success(setmealDishPage);
    }


    /**
     * 获取套餐id数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getByID(@PathVariable Long id) {
        SetmealDto list = setmealService.getByIdWithSetmeal(id);
        return R.success(list);
    }

    /**
     * 修改套餐信息
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        setmealService.updateWithSeteal(setmealDto);
        return R.success("修改成功");
    }
}
