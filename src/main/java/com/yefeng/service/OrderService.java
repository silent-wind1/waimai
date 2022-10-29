package com.yefeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yefeng.entity.Orders;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}