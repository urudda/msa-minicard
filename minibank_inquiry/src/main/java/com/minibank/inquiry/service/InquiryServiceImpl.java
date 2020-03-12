package com.minibank.inquiry.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minibank.inquiry.domain.entity.Account;
import com.minibank.inquiry.domain.entity.Customer;
import com.minibank.inquiry.domain.entity.RecentWork;
import com.minibank.inquiry.domain.entity.TransactionHistory;
import com.minibank.inquiry.domain.entity.TransferHistory;
import com.minibank.inquiry.domain.repository.InquiryRepository;
import com.minibank.inquiry.exception.BusinessException;
import com.minibank.inquiry.exception.SystemException;

@Service("inquiryService")
public class InquiryServiceImpl implements InquiryService{
	private final Logger LOGGER = LoggerFactory.getLogger(InquiryServiceImpl.class);

	// 거래 이력 저장을 위한  상수갑 지정
	private static final String CREATE_CUSTOMER_CD = "1"; // 고객생성
	private static final String CREATE_ACCOUNT_CD = "2"; // 계좌생성
	private static final String DEPOSIT_CD = "3"; // 입금발생
	private static final String WITHDRAW_CD = "4"; // 출금발생
	private static final String TRANSFER_CD = "5"; // 이체발생
	private static final String B2B_TRANSFER_CD = "6"; // 타행이체 발생
	
	private static final String FAIL_CD = "2";
	private static final String SUCCESS_CD = "3";
    
    @Autowired
    InquiryRepository inquiryRepository;

    @Override
    public Customer retrieveCustomerDetail(String cstmId) throws Exception {
    	Customer customer = null;
    	
    	// 고객 정보 조회
		customer = inquiryRepository.selectCustomer(Customer.builder().cstmId(cstmId).build());
	
		if (customer == null) 
			throw new BusinessException("존재하지 않는 아이디입니다.");
		
		// 계좌 목록 조회
		List<Account> accountList = retrieveAccountList(cstmId);   
		customer.addAllAccounts(accountList);
    	
        return customer;
    }

    /**
     * 사용자 id에  계좌 목록을 조회합니다.
     * @param cstmId
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public List<Account> retrieveAccountList(String cstmId) throws Exception {
    	List<Account> accountList = null;
		
		accountList= inquiryRepository.selectAccountList(Account.builder().cstmId(cstmId).build());
		
		if (accountList == null || accountList.size() <=0)
			throw new BusinessException(cstmId + " 님의 계좌 목록 정보를 조회하지 못했습니다.");
    	
    	return accountList;
    }
    
    /**
     * 사용자 정보를 생성합니다.
     */
    @Override
	public int createCustomer(Customer customer) throws Exception {
		return inquiryRepository.insertCustomer(customer);
	}

    /**
     * 계좌 정보를 생성합니다.
     */
	@Override
	public int createAccount(Account account) throws Exception {
		return inquiryRepository.insertAccount(account);
	}

	/**
	 * 이체 정보를 생성합니다.
	 */
	@Override
	public int updateTransferLimit(Customer customer) throws Exception {
		return inquiryRepository.updateTransferLimit(customer);
	}

	/**
	 * 계좌 잔액을 업데이트 합니다.
	 */
	@Override
	public int updateAccountBalance(Account account) throws Exception {
		return inquiryRepository.updateAccountBalance(account);
	}
	
	/**
	 * 휴먼 고객 정보를 조회합니다.
	 */
	@Override
	public List<RecentWork> retrieveDormantCustomerList() throws Exception {
		return inquiryRepository.selectDormantCustomerList();
	}
    
	/**
	 * 최근 사용자 작업 이력을 기록합니다.
	 */
	@Override
	public int updateCreatingCustomerWork(Customer customer) throws Exception {
		int ret = 0;
		
		ret = inquiryRepository.insertCreatingCustomerWork(RecentWork.builder()
				.rcntWorkDivCd(CREATE_CUSTOMER_CD)
				.workStsCd(SUCCESS_CD)
				.cstmId(customer.getCstmId())
				.cstmNm(customer.getCstmNm())
				.build());
		
		return ret;
	}

	/**
	 * 계정 정보 이력을 저장합니다.
	 */
	@Override
	public int updateCreatingAccountWork(Account account) throws Exception {
		int ret = 0;
		
		ret = inquiryRepository.updateCreatingAccountWork(RecentWork.builder()
				.rcntWorkDivCd(CREATE_ACCOUNT_CD)
				.workAcntNo(account.getAcntNo())
				.workStsCd(SUCCESS_CD)
				.cstmId(account.getCstmId())
				.cstmNm(account.getCstmNm())
				.build());
		
		return ret;
	}

	/**
	 * 이체 정보 이력을 저장합니다.
	 */
	@Override
	@Transactional
	public int updateTransactionWork(TransactionHistory transaction) throws SystemException {
		int ret = 0;
		try {
			ret = inquiryRepository.updateTransactionWork(RecentWork.builder()
					.rcntWorkDivCd("D".equals(transaction.getDivCd())?DEPOSIT_CD:WITHDRAW_CD)
					.workStsCd("1".equals(transaction.getStsCd())?SUCCESS_CD:FAIL_CD)
					.workAcntNo(transaction.getAcntNo())
					.workTrnsAmt(transaction.getTrnsAmt())
					.build());
		} catch (Exception e) {
    		String msg = "최근 이체 이력을 업데이트 하는 중 에러가 발생했습니다.";
    		LOGGER.error(msg, e);
			throw new SystemException(msg);
		}
		return ret;
		
	}

	/**
	 * 타행 이체 정보 이력을 저장합니다.
	 */
	@Override
	public int updateTransferWork(TransferHistory transfer) throws Exception {
		int ret = 0;
		
		ret = inquiryRepository.updateTransferWork(RecentWork.builder()
				.rcntWorkDivCd("1".equals(transfer.getDivCd())?TRANSFER_CD:B2B_TRANSFER_CD)
				.workStsCd(transfer.getStsCd())
				.cstmId(transfer.getCstmId())
				.workAcntNo(transfer.getWthdAcntNo())
				.workTrnsAmt(transfer.getTrnfAmt())
				.build());
		
		return ret;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int createAccountAndUpdateAccountWork(Account account) throws Exception {
		return createAccount(account) > 0 ? (updateCreatingAccountWork(account) > 0 ? 1 : 0) : 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int createCustomerAndCustomerWork(Customer customer) throws Exception {
		return createCustomer(customer) > 0 ? (updateCreatingCustomerWork(customer) > 0 ? 1 : 0) : 0;
	}

}
