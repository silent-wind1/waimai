package com.yefeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yefeng.common.R;
import com.yefeng.dto.DishDto;
import com.yefeng.dto.SermealDishDto;
import com.yefeng.dto.SetmealDto;
import com.yefeng.entity.Category;
import com.yefeng.entity.Dish;
import com.yefeng.entity.Setmeal;
import com.yefeng.entity.SetmealDish;
import com.yefeng.service.CategoryService;
import com.yefeng.service.DishService;
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
    private CategoryService categoryService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private DishService dishService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("setmealDto={}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }

    /**
     * 套餐分页查询
     *
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
     *
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
     *
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
     *
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
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> sale(@PathVariable int status, String[] ids) {
        for (String id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }
        return R.success("修改成功");
    }

    /**
     * 获取套餐接口
     *
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


//    @GetMapping("/dish/{id}")
//    public R<List<SermealDishDto>> dishById(SetmealDto SetmealDto) {
//        log.info("/dish/{id}={}", SetmealDto);
//        // 查询到该套餐里的菜品信息
//        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(SetmealDish::getSetmealId, SetmealDto.getId());
//        List<SetmealDish> list = setmealDishService.list(queryWrapper);
//
//        List<SermealDishDto> setmealDtos = list.stream().map(item -> {
//            SermealDishDto sermealDishDto = new SermealDishDto();
//            BeanUtils.copyProperties(item, sermealDishDto);
//            // 查询菜品信息里的图片
//            Dish dish = dishService.getById(item.getDishId());
//            sermealDishDto.setImage(dish.getImage());
//            return sermealDishDto;
//        }).collect(Collectors.toList());
//        return R.success(setmealDtos);
//    }

    /**
     * 移动端点击套餐展示菜品信息
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public R<List<DishDto>> showSetmealDish(@PathVariable Long id) {
        //条件构造器
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //手里的数据只有setmealId
        dishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        //查询数据
        List<SetmealDish> records = setmealDishService.list(dishLambdaQueryWrapper);
        List<DishDto> dtoList = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            //copy数据
            BeanUtils.copyProperties(item,dishDto);
            //查询对应菜品id
            Long dishId = item.getDishId();
            //根据菜品id获取具体菜品数据，这里要自动装配 dishService
            Dish dish = dishService.getById(dishId);
            //其实主要数据是要那个图片，不过我们这里多copy一点也没事
            BeanUtils.copyProperties(dish, dishDto);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dtoList);
    }
}
