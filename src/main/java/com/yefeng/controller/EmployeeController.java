package com.yefeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yefeng.common.R;
import com.yefeng.entity.Employee;
import com.yefeng.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 根据页面提交用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        System.out.println(employee.getUsername());
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if(emp == null) {
            return R.error("用户不存在");
        }

        if(!emp.getPassword().equals(password)) {
            return R.error("密码不正确");
        }

        if(emp.getStatus() == 0) {
            return  R.error("账号已经被禁用");
        }

        // 登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);
    }


    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }



    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        long id = Thread.currentThread().getId() ;
        log.info("线程id:{}" ,id);
        log.info("新增员工，员工信息：{}", employee.toString());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        Long empID = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser(empID);
//        employee.setUpdateUser(empID);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 构造分页器
        Page pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        // 添加过滤查询
        wrapper.like(StringUtils.hasLength(name), Employee::getName, name);
        // 添加排序条件
        wrapper.orderByDesc(Employee::getUpdateTime);
        // 执行
        employeeService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }


    /**
     * 修改员工信息
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("employee={}", employee.toString());
        // 获取当前线程id唯一标识
        long id = Thread.currentThread().getId() ;
        log.info("线程id:{}" ,id);
        // 先获取到前端传递过来的id
//        Long id = (Long) request.getSession().getAttribute("employee");
        // 通过id去禁用该用户
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(id);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 获取编辑员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getEmployee(@PathVariable String id) {
        Employee employee = employeeService.getById(id);
        if(employee != null) {
            return R.success(employee);
        }
//        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
//        wrapper.eq("id", id);
//        Employee employee = employeeService.getOne(wrapper);
        return R.error("没有查询到此用户");
    }
}
