package com.group3.server.utils;

import java.math.BigDecimal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.group3.server.models.auth.User;

public class AuthUtils {
    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) auth.getPrincipal();
        return userDetails.getId(); // id của user hiện tại
    }

    public static BigDecimal getUserBalance() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) auth.getPrincipal();
        return userDetails.getBalance(); // id của user hiện tại
    }

    public static String getCurrentCitizenID() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) auth.getPrincipal();
        return userDetails.getCitizenID();
    }

    public static String getGroupName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) auth.getPrincipal();
        return userDetails.getGroup().getName(); // tên của nhóm hiện tại
    }
}
