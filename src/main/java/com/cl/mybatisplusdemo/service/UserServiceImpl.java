package com.cl.mybatisplusdemo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cl.mybatisplusdemo.model.User;
import com.cl.mybatisplusdemo.dao.UserDao;
import org.springframework.stereotype.Service;

/**
 * @author cl
 * @version V1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
}
