package com.hanjixin.core.service;

import com.hanjixin.core.pojo.address.Address;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public interface AddressService {

    List<Address> findListByLoginUser(String name);
}
