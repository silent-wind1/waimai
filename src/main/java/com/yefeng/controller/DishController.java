package com.yefeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yefeng.common.R;
import com.yefeng.dto.DishDto;
import com.yefeng.entity.Category;
import com.yefeng.entity.Dish;
import com.yefeng.service.CategoryService;
import com.yefeng.service.DishFlavorService;
import com.yefeng.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("dishDto={}", dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("添加分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.like(name != null, "name", name);
        wrapper.orderByDesc("update_time");
        dishService.page(dishPage, wrapper);
        // 将dishPage中的属性值拷贝到dishDtoPage忽略Page类中的records
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        // 获取dishPage中的Records
        List<Dish> records = dishPage.getRecords();
        // 遍历records中的数据
        List<DishDto> list = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            // 将item拷贝到dishDto中去
            BeanUtils.copyProperties(item, dishDto);
            // 获取到categoryid
            Long categoryId = item.getCategoryId();
            // 查询到categoryid这条数据
            Category category = categoryService.getById(categoryId);
            // 如果查到了category这条件数据，获取到categoryName并添加到dishDto中，并且返回
            if(category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 获取修改菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }


    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    /**
     * 停售起售菜品
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> sale(@PathVariable int status, int[] ids) {
        for (int id : ids) {
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }
        return R.success("修改成功");
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(String[] ids) {
        for (String id : ids) {
            dishService.removeById(id);
        }
        return R.success("删除成功");
    }

    /**
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getStatus, 1);
        wrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(wrapper);
        return R.success(list);
    }
}
