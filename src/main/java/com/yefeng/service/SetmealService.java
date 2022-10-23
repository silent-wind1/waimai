package com.yefeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yefeng.dto.SetmealDto;
import com.yefeng.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);
}
