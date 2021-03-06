package com.minibank.inquiry.domain.repository;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.minibank.inquiry.domain.entity.Account;
import com.minibank.inquiry.domain.entity.Customer;

@Mapper
public interface InquiryRepository {
	
    int insertCustomer(Customer customer) throws Exception;
    int insertAccount(Account account) throws Exception;
    int updateTransferLimit(Customer customer) throws Exception;
    int updateAccountBalance(Account account) throws Exception;
    Customer selectCustomer(Customer customer) throws Exception;
    List<Account> selectAccountList(Account account) throws Exception;
    
}
