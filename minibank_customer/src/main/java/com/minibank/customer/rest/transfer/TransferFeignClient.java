package com.minibank.customer.rest.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.minibank.customer.exception.SystemException;
import com.minibank.customer.rest.transfer.entity.TransferLimit;

import feign.hystrix.FallbackFactory;

//Eureka를 사용 할 경우 FeignClien의 name은 eureka의 service명과 동일하게 설정한다.
//대신 기존 URL명을 생략
@FeignClient(name = "minibank-transfer", 
			//url="http://localhost:8070", 
			fallbackFactory = TransferFeignClientFallbackFactory.class)
public interface TransferFeignClient {
	
	@PostMapping("/minibank/transfer/transfer-limit/rest/v0.8")
	public Integer createTransferLimit(@RequestBody TransferLimit transferLimit) throws Exception;

	@GetMapping("/minibank/transfer/transfer-limit/rest/v0.8/{cstmId}")
	public TransferLimit retrieveTransferLimit(@PathVariable("cstmId") String cstmId) throws Exception;
	
}

@Component
class TransferFeignClientFallbackFactory implements FallbackFactory<TransferFeignClient> {
	@Override
	public TransferFeignClient create(Throwable t) {
		return new TransferFeignClient() {
			private final Logger LOGGER = LoggerFactory.getLogger(TransferFeignClient.class);

			@Override
			public TransferLimit retrieveTransferLimit(String cstmId)  throws Exception {
				String msg = "feignClient를 이용한 " + cstmId + " 사용자의 이체한도 서비스 호출에 문제가 있습니다.";
				LOGGER.error(msg, t);
				// 필요하다면 fallback 처리
				throw new Exception();
				// 여기서 throw new Excepiton을 넘기지 않고 임의의 리턴 값을 넘겨서 시스템이 중단 없이 흘러가게 하는 것도 가능  
			}

			@Override
			public Integer createTransferLimit(TransferLimit transferLimit) throws Exception{
				String msg = "feignClient를 이용한 " + transferLimit.getCstmId() + " 사용자의 이체한도 저장 서비스에 문제가 있습니다.";
				LOGGER.error(msg, t);
				throw new Exception();
				// 여기서 throw new Excepiton을 넘기지 않고 임의의 리턴 값을 넘겨서 시스템이 중단 없이 흘러가게 하는 것도 가능 
			}

		};
	}

}
