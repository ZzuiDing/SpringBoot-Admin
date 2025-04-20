package com.example.spba.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


public interface wallerService {
    BigDecimal getBalance(int loginIdAsInt);

    boolean pay(List<Integer> orderIds);
}
