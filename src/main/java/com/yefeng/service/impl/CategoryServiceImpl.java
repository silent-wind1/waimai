package com.yefeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.entity.Category;
import com.yefeng.mapper.CategoryMapper;
import com.yefeng.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
