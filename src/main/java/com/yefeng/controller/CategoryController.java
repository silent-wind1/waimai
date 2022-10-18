package com.yefeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yefeng.common.R;
import com.yefeng.entity.Category;
import com.yefeng.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /**
     * 添加菜品与套餐分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category={}", category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询菜品
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        log.info("/category - >page={}, pageSize={}", page, pageSize);
        Page pageInfo = new Page(page, pageSize);
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        categoryService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }

    /**
     * 修改菜品分类与套餐分类
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("put -> category={}", category);
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("delete -> id={}", ids);
//        categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("删除成功");
    }
}
