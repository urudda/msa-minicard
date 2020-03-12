package com.minibank.customer.rest.account;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.minibank.customer.exception.SystemException;
import com.minibank.customer.exception.advice.DefaultExceptionAdvice;
import com.minibank.customer.rest.account.entity.Account;
import com.minibank.customer.rest.transfer.TransferFeignClient;

import feign.hystrix.FallbackFactory;

@FeignClient(name = "minibank-account", 
//           url="http://localhost:8070", 
			 fallbackFactory = AccountFeignClientFallbackFactory.class)
public interface AccountFeignClient {

	@GetMapping("/minibank/account/list/rest/v0.8/{cstmId}")
	List<Account> retrieveAccountList(@PathVariable("cstmId") String cstmId) throws Exception;

}

@Component
class AccountFeignClientFallbackFactory implements FallbackFactory<AccountFeignClient> {
	@Override
	public AccountFeignClient create(Throwable t) {
		return new AccountFeignClient() {
			private final Logger LOGGER = LoggerFactory.getLogger(AccountFeignClientFallbackFactory.class);
			
			@Override
			public List<Account> retrieveAccountList(String cstmId) throws Exception {
				// 외부 통신에러시 필요한 후속 조치를 여기서 설정 가능합니다.
				// To Do
				
				String msg = "feignClient를 이용한 " + cstmId + " 사용자의 계정 리스트 호출에 문제가 있습니다.";
				LOGGER.error(msg, t);
				throw new Exception();
			}

		};
	}

}