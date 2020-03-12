package com.minibank.transfer.rest.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.minibank.transfer.rest.account.entity.Account;
import com.minibank.transfer.rest.account.entity.TransactionHistory;
import com.minibank.transfer.rest.account.entity.TransactionResult;

import feign.hystrix.FallbackFactory;

@FeignClient(name = "minibank-account", 
//           url="http://localhost:8070", 
			 fallbackFactory = AccountFeignClientFallbackFactory.class)
public interface AccountFeignClient {

	@PostMapping("/minibank/account/withdraw/rest/v0.8")
	TransactionResult withdraw(@RequestBody TransactionHistory transaction) throws Exception;
	
	@PostMapping("/minibank/account/deposit/rest/v0.8")
	TransactionResult deposit(@RequestBody TransactionHistory transaction) throws Exception;
	
	@GetMapping("/minibank/account/rest/v0.8/{acntNo}")
	Account retrieveAccount(@PathVariable("acntNo") String acntNo) throws Exception;
	
	@PostMapping("/minibank/account/withdraw/cancel/rest/v0.8")
	int cancelWithdraw(@RequestBody TransactionHistory transaction) throws Exception;	
	
}

@Component
class AccountFeignClientFallbackFactory implements FallbackFactory<AccountFeignClient> {
	@Override
	public AccountFeignClient create(Throwable t) {
		return new AccountFeignClient() {
			private final Logger LOGGER = LoggerFactory.getLogger(AccountFeignClient.class);

			@Override
			public TransactionResult withdraw(TransactionHistory transaction) throws Exception {				
				String msg = "feignClient를 이용한 사용자의 출금 서비스 호출에 문제가 있습니다.";
				LOGGER.error(msg, t);
				throw new Exception(msg);
			}
			
			@Override
			public TransactionResult deposit(TransactionHistory transaction) throws Exception {				
				String msg = "feignClient를 이용한 사용자의 입금 서비스 호출에 문제가 있습니다.";
				LOGGER.error(msg, t);
				throw new Exception(msg);
			}

			@Override
			public Account retrieveAccount(String acntNo) throws Exception {
	        	String msg = "feignClient를 이용한 " + acntNo + " 계좌 조회 서비스 호출에 문제가 있습니다.";
				LOGGER.error(msg, t);
				throw new Exception(msg);
			}

			@Override
			public int cancelWithdraw(TransactionHistory transaction) throws Exception {
	        	String msg = "feignClient를 이용한 " + transaction.getAcntNo() + " 계좌 조회 서비스 호출에 문제가 있습니다.";
				LOGGER.error(msg, t);
				throw new Exception(msg);
			}
		};
	}
}