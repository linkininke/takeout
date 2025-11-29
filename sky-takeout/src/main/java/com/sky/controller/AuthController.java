package com.sky.controller;

import com.sky.dto.LoginDTO;
import com.sky.dto.WxLoginDTO;
import com.sky.entity.Customer;
import com.sky.entity.User;
import com.sky.service.CustomerService;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.utils.WxUtil;
import com.sky.vo.Result;
import com.sky.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private WxUtil wxUtil;

    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody LoginDTO loginDTO) {
        // 认证用户
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成JWT令牌
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("roleId", user.getRoleId());

        String token = jwtUtil.generateToken(claims, user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        // 构建返回结果
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setName(user.getName());
        userVO.setPhone(user.getPhone());
        userVO.setEmail(user.getEmail());
        userVO.setRoleId(user.getRoleId());
        userVO.setToken(token);
        userVO.setRefreshToken(refreshToken);

        return Result.success(userVO);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        SecurityContextHolder.clearContext();
        return Result.success();
    }

    /**
     * 微信授权登录
     * @param wxLoginDTO 微信登录DTO
     * @return 登录结果
     */
    @PostMapping("/wx-login")
    public Result<Map<String, Object>> wxLogin(@RequestBody WxLoginDTO wxLoginDTO) {
        // 1. 获取微信openid
        String openid = wxUtil.getOpenid(wxLoginDTO.getCode());

        // 2. 根据openid查询顾客信息
        Customer customer = customerService.getByOpenid(openid);

        // 3. 如果顾客不存在，创建新顾客
        if (customer == null) {
            customer = new Customer();
            customer.setOpenid(openid);
            customer.setStatus(1);
            customer.setCreateTime(LocalDateTime.now());
            customer.setUpdateTime(LocalDateTime.now());
            customerService.save(customer);
        }

        // 4. 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("customerId", customer.getId());
        String token = jwtUtil.generateToken(claims, openid);

        // 5. 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("customer", customer);

        return Result.success(result);
    }

}