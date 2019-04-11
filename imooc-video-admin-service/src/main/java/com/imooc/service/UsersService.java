package com.imooc.service;

import com.imooc.pojo.Users;
import com.imooc.utils.PagedResult;

public interface UsersService {
	PagedResult queryUsers(Users user, Integer page, Integer pageSize);
}
