package com.yefeng;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yefeng.entity.Employee;
import com.yefeng.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Random;

@SpringBootTest
class XiaofangwaimaiApplicationTests {
    @Autowired
    private EmployeeService employeeService;

    @Test
    void contextLoads() {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, "admin");
        Employee emp = employeeService.getOne(queryWrapper);
        System.out.println(emp);
    }

    /**
     * 添加假数据
     */
    @Test
    void addEmployee() {
        String[] name = {
                "乔琼", "紫玲", "易文", "焯文", "卓娅", "羿瑶", "茂霞", "娟妮", "谨花", "恩芳", "坤颖", "惠媛"
        };
        String[] username = {
                "qiaojing", "ziling", "yiwen", "chaowen", "zhuoya", "yiyao", "shengxia", "juanne", "jinghua", "enfang", "kunyi", "huinuan"
        };
        String[] setIdNumber = {
                "440922196406267522",
                "110101199001010042",
                "440922200106267132",
                "340922200106267522",
                "420922200106267523",
                "440922200102675124",
                "410922200201267521",
                "540922200307267529",
                "640922200409267522",
                "140922200501267522",
                "240922200606067522",
                "640922200802267522",
        };
        Employee employee = new Employee();
        for (int i = 0; i < 11; i++) {
//            employee.setName(name[new Random().nextInt(11)]);
//            employee.setUsername(name[new Random().nextInt(11)]);
            employee.setId(i + 2L);
            employee.setName(name[i]);
            employee.setUsername(username[i]);
            employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
            employee.setSex(String.valueOf(new Random().nextInt(2)));
            employee.setIdNumber(setIdNumber[i]);
            employee.setStatus(1);
            employee.setPhone("1951570706");
            employee.setCreateTime(LocalDateTime.now());
            employee.setUpdateTime(LocalDateTime.now());
            employee.setCreateUser(1L);
            employee.setUpdateUser(1L);
            employeeService.save(employee);
        }
        System.out.println("添加成功");
    }


    @Test
    void subString() {
        String fileName = "yefeng.jpg";;
        String substring = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(substring);
    }
}
