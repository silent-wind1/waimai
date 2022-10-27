package com.yefeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.entity.AddressBook;
import com.yefeng.mapper.AddressBookMapper;
import com.yefeng.service.AddreddBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddreddBookService {
}