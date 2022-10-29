package com.yefeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yefeng.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMpper extends BaseMapper<Orders> {
}