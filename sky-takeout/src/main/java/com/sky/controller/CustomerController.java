package com.sky.controller;

import com.sky.entity.Customer;
import com.sky.service.CustomerService;
import com.sky.utils.CurrentUserUtil;
import com.sky.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 获取当前登录顾客信息
     * @return 顾客信息
     */
    @GetMapping("/current")
    public Result<Customer> getCurrentCustomer() {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        Customer customer = customerService.getById(customerId);
        return Result.success(customer);
    }

    /**
     * 根据ID获取顾客信息
     * @param id 顾客ID
     * @return 顾客信息
     */
    @GetMapping("/{id}")
    public Result<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getById(id);
        return Result.success(customer);
    }

    /**
     * 根据openid获取顾客信息
     * @param openid 微信openid
     * @return 顾客信息
     */
    @GetMapping("/openid/{openid}")
    public Result<Customer> getCustomerByOpenid(@PathVariable String openid) {
        Customer customer = customerService.getByOpenid(openid);
        return Result.success(customer);
    }

    /**
     * 保存顾客信息
     * @param customer 顾客信息
     * @return 保存结果
     */
    @PostMapping
    public Result<Customer> saveCustomer(@RequestBody Customer customer) {
        customerService.save(customer);
        return Result.success(customer);
    }

    /**
     * 更新当前登录顾客信息
     * @param customer 顾客信息
     * @return 更新结果
     */
    @PutMapping("/current")
    public Result<Customer> updateCurrentCustomer(@RequestBody Customer customer) {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        customer.setId(customerId);
        customerService.updateById(customer);
        return Result.success(customer);
    }

    /**
     * 更新顾客信息
     * @param id 顾客ID
     * @param customer 顾客信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setId(id);
        customerService.updateById(customer);
        return Result.success(customer);
    }
}