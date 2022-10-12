package com.yefeng;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yefeng.entity.Employee;
import com.yefeng.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XiaofangwaimaiApplicationTests {
    @Autowired
    private EmployeeService employeeService;

    @Test
    void contextLoads() {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, "root");
        Employee emp = employeeService.getOne(queryWrapper);
        System.out.println(emp);
    }

}
