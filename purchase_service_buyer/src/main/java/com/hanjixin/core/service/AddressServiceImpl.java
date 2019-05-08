package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hanjixin.core.dao.address.AddressDao;
import com.hanjixin.core.pojo.address.Address;
import com.hanjixin.core.pojo.address.AddressQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 地址管理
 */
@Service
@Transactional
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressDao addressDao;
    @Override
    public List<Address> findListByLoginUser(String name) {
        AddressQuery addressQuery = new AddressQuery();
        addressQuery.createCriteria().andUserIdEqualTo(name);
        return addressDao.selectByExample(addressQuery);
    }
}
