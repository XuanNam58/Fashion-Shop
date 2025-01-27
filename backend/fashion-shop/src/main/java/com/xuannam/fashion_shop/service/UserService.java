package com.xuannam.fashion_shop.service;

import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.UserException;

public interface UserService {
    User findUserById(Long userId) throws UserException;
    User findUserProfileByJwt(String jwt) throws UserException;
}
