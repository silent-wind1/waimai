package com.yefeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yefeng.common.BaseContext;
import com.yefeng.common.R;
import com.yefeng.entity.AddressBook;
import com.yefeng.service.AddreddBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddreddBookService addressBookService;

    /**
     * 新增
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("setDefault -> AddressBook={}", addressBook);
        //获取当前用户id
        addressBook.setUserId(BaseContext.getCurrentId());
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        //SQL:update address_book set is_default = 0 where user_id = ?
        wrapper.eq(addressBook.getUserId()!=null, AddressBook::getUserId, addressBook.getUserId());
        wrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(wrapper);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        }
        return R.error("没有找到对象");
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(wrapper);
        if (addressBook != null) {
            return R.success(addressBook);
        }
        return R.error("没有找到");
    }

    /**
     * 查询指定用户的全部地址
     * @param addressBook
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook={}", addressBook);
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
        wrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = addressBookService.list(wrapper);
        if(list != null) {
            return R.success(list);
        }
        return R.error("没有数据");
    }
}