package com.hanjixin.core.service;

import entity.MorrisData;

import java.util.List;

public interface SalesService {
    List<MorrisData> salesPre(String start, String end,String username);
}
