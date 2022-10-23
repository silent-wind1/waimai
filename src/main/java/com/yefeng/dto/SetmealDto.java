package com.yefeng.dto;

import com.yefeng.entity.Setmeal;
import com.yefeng.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
