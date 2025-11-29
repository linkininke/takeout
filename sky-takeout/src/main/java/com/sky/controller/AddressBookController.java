package com.sky.controller;

import com.sky.entity.AddressBook;
import com.sky.service.AddressBookService;
import com.sky.utils.CurrentUserUtil;
import com.sky.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address-book")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 获取当前登录顾客的地址列表
     * @return 地址列表
     */
    @GetMapping
    public Result<List<AddressBook>> getAddressBookList() {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        List<AddressBook> addressBooks = addressBookService.getByCustomerId(customerId);
        return Result.success(addressBooks);
    }

    /**
     * 根据顾客ID获取地址列表
     * @param customerId 顾客ID
     * @return 地址列表
     */
    @GetMapping("/customer/{customerId}")
    public Result<List<AddressBook>> getAddressBookByCustomerId(@PathVariable Long customerId) {
        List<AddressBook> addressBooks = addressBookService.getByCustomerId(customerId);
        return Result.success(addressBooks);
    }

    /**
     * 获取当前登录顾客的默认地址
     * @return 默认地址
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefaultAddress() {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        AddressBook defaultAddress = addressBookService.getDefaultAddress(customerId);
        return Result.success(defaultAddress);
    }

    /**
     * 获取默认地址
     * @param customerId 顾客ID
     * @return 默认地址
     */
    @GetMapping("/default/{customerId}")
    public Result<AddressBook> getDefaultAddress(@PathVariable Long customerId) {
        AddressBook defaultAddress = addressBookService.getDefaultAddress(customerId);
        return Result.success(defaultAddress);
    }

    /**
     * 获取地址详情
     * @param id 地址ID
     * @return 地址详情
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getAddressBookById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 添加地址
     * @param addressBook 地址信息
     * @return 添加结果
     */
    @PostMapping
    public Result<AddressBook> addAddressBook(@RequestBody AddressBook addressBook) {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        addressBook.setCustomerId(customerId);
        // 如果是默认地址，先将其他地址设为非默认
        if (addressBook.getIsDefault() == 1) {
            addressBookService.setNonDefault(customerId);
        }
        addressBookService.save(addressBook);
        return Result.success(addressBook);
    }

    /**
     * 更新地址
     * @param id 地址ID
     * @param addressBook 地址信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<AddressBook> updateAddressBook(@PathVariable Long id, @RequestBody AddressBook addressBook) {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        addressBook.setId(id);
        addressBook.setCustomerId(customerId);
        // 如果是默认地址，先将其他地址设为非默认
        if (addressBook.getIsDefault() == 1) {
            addressBookService.setNonDefault(customerId);
        }
        addressBookService.updateById(addressBook);
        return Result.success(addressBook);
    }

    /**
     * 删除地址
     * @param id 地址ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteAddressBook(@PathVariable Long id) {
        addressBookService.removeById(id);
        return Result.success();
    }

    /**
     * 设置默认地址
     * @param id 地址ID
     * @return 设置结果
     */
    @PutMapping("/default/{id}")
    public Result<AddressBook> setDefaultAddress(@PathVariable Long id) {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        // 先将其他地址设为非默认
        addressBookService.setNonDefault(customerId);
        // 将当前地址设为默认
        AddressBook addressBook = addressBookService.getById(id);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return Result.success(addressBook);
    }
}