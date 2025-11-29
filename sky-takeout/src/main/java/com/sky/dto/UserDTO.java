package com.sky.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String username;

    private String password;

    private String name;

    private String phone;

    private String email;

    private Integer status;

    private Long roleId;

}