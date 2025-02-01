package com.storesmanagementsystem.gateway.service;

import com.storesmanagementsystem.gateway.contracts.UserInfoBean;

public interface UserService {

    UserInfoBean getUserByUserId(Integer userId);
    UserInfoBean getUser(String username);

}
