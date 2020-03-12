package com.minibank.account.rest.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.minibank.account.rest.customer.entity.Customer;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service("customerComposite")
public class CustomerCompositeImpl implements CustomerComposite {

    private static Logger LOGGER = LoggerFactory.getLogger(CustomerCompositeImpl.class);
	
    @Value("${customer.api.url}")
    private String CUSTOMER_API_URL;
    
    @LoadBalanced
    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
    
    @Autowired
    RestTemplate restTemplate;
    
    @HystrixCommand(commandKey="retrieveCustomer", fallbackMethod="fallbackRetriveCustomer")
	@Override
	public Customer retrieveCustomer(String cstmId) throws Exception {
        String apiUrl =  "/rest/v0.8/{cstmId}";
		return this.restTemplate.getForObject(CUSTOMER_API_URL + apiUrl, Customer.class, cstmId);
	}

    public Customer fallbackRetriveCustomer(String cstmId, Throwable t) throws Exception {
    	String msg = "restTemplate를 이용하여 " + cstmId + " 고객정보 조회 서비스 호출에 문제가 있습니다.";
    	LOGGER.error(msg, t);
        throw new Exception();
    }
}
