package com.sky.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.dto.LoginDTO;
import com.sky.dto.WxLoginDTO;
import com.sky.entity.Customer;
import com.sky.service.CustomerService;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.utils.WxUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;
    
    @MockBean
    private CustomerService customerService;

    @MockBean
    private WxUtil wxUtil;

    @Test
    void testLogin() throws Exception {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("123456");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(loginDTO);

        // 执行测试
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testWxLogin() throws Exception {
        // 准备测试数据
        WxLoginDTO wxLoginDTO = new WxLoginDTO();
        wxLoginDTO.setCode("test-code");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(wxLoginDTO);

        // 模拟微信工具类返回openid
        when(wxUtil.getOpenid(any())).thenReturn("test-openid");

        // 模拟顾客服务返回顾客信息
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setOpenid("test-openid");
        when(customerService.getByOpenid(any())).thenReturn(customer);

        // 模拟JWT工具类生成token
        when(jwtUtil.generateToken(any(), any())).thenReturn("test-token");

        // 执行测试
        mockMvc.perform(post("/auth/wx-login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }
}
