package com.yefeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yefeng.common.R;
import com.yefeng.dto.SetmealDto;
import com.yefeng.entity.Category;
import com.yefeng.entity.Setmeal;
import com.yefeng.service.CategoryService;
import com.yefeng.service.SetmealDishService;
import com.yefeng.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("setmealDto={}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDishPage = new Page<>();
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        // 查询
        wrapper.like(name != null, Setmeal::getName, name);
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, wrapper);
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, setmealDishPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

                List<SetmealDto> list = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            // 拷贝对象
            BeanUtils.copyProperties(item, setmealDto);
            // 分类id
            Long categoryId = item.getCategoryId();
            // 根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String setmealName = category.getName();
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

    /**
     * 删除套餐信息
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids={}", ids);
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }

    /**
     * 停售套餐
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> sale(@PathVariable int status, String [] ids) {
        for (String id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }
        return R.success("修改成功");
    }

    /**
     * 获取套餐接口
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId, setmeal.getCategoryId());
        wrapper.eq(Setmeal::getStatus, setmeal.getStatus());
        List<Setmeal> list = setmealService.list(wrapper);
        return R.success(list);
    }
}
