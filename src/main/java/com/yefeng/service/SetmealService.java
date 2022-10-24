package com.yefeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yefeng.dto.SetmealDto;
import com.yefeng.entity.Setmeal;
import com.yefeng.entity.SetmealDish;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    SetmealDto getByIdWithSetmeal(Long id);

    void updateWithSeteal(SetmealDto setmealDto);
}
