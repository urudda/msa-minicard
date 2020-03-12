package com.minibank.inquiry.service;

import java.util.List;

import com.minibank.inquiry.domain.entity.Account;
import com.minibank.inquiry.domain.entity.Customer;
import com.minibank.inquiry.domain.entity.RecentWork;
import com.minibank.inquiry.domain.entity.TransactionHistory;
import com.minibank.inquiry.domain.entity.TransferHistory;

public interface InquiryService {
    public Customer retrieveCustomerDetail(String cstmId) throws Exception;
    public int createCustomer(Customer customer) throws Exception;
    public int createAccount(Account account) throws Exception;
    public int updateTransferLimit(Customer customer) throws Exception;
    public int updateAccountBalance(Account account) throws Exception;
	public List<RecentWork> retrieveDormantCustomerList() throws Exception;
    public int updateCreatingCustomerWork(Customer customer) throws Exception;
    public int updateCreatingAccountWork(Account account) throws Exception;
    public int updateTransactionWork(TransactionHistory transaction) throws Exception;
    public int updateTransferWork(TransferHistory transfer) throws Exception;
    public int createAccountAndUpdateAccountWork(Account account) throws Exception;
    public int createCustomerAndCustomerWork(Customer customer) throws Exception;
}
