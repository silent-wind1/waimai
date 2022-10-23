package com.yefeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.dto.SetmealDto;
import com.yefeng.entity.Setmeal;
import com.yefeng.entity.SetmealDish;
import com.yefeng.mapper.SetmealDishMapper;
import com.yefeng.mapper.SetmealMapper;
import com.yefeng.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SetmealDishServicelmpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {

}
