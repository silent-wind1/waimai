package com.yefeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yefeng.common.R;
import com.yefeng.common.TencentSmsScript;
import com.yefeng.entity.User;
import com.yefeng.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 发送手机验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        // 获取手机号
        String phone = user.getPhone();
        if (!phone.isEmpty()) {
            TencentSmsScript tencentSmsScript = new TencentSmsScript();
            String[] code = tencentSmsScript.achieveCode(6);
            log.info("验证码={}", code);
            // 将验证码存入redis中，并设置5分钟有效期
            redisTemplate.opsForValue().set(phone, code[0], 5, TimeUnit.MINUTES);
            String[] phone1 = tencentSmsScript.copyPhone(phone);
            // 发送短信
//            tencentSmsScript.sendMsg(phone1, code);
            // 存入session中
//            session.setAttribute(phone, code);
            return R.success("发送成功");
        }
        return R.success("发送失败");
    }

    /**
     * 手机登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        // 获取手机号
        String phone = map.get("phone").toString();
        // 获取验证码
        String code = map.get("code").toString();
        // 从Session中获取保存的验证码
//        Object codeInSession = session.getAttribute(phone);
        // 从redis中获取保存的验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);
        log.info("phone={}, code={}, codeInSession={}", phone, code, codeInSession);
        // 进行验证码的比对（页面提交的验证码和Session中的保存的验证码比对）
        if (code != null && code.equals(codeInSession)) {
            //如果能够比对成功，说明登录成功
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            //判断依据是从数据库中查询是否有其邮箱
            wrapper.eq("phone", phone);
            User user = userService.getOne(wrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
                user.setName("用户" + codeInSession);
            }
            session.setAttribute("user", user.getId());
            // 登录成功将手机验证码删除
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败");
    }

    /**
     * 退出
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}


