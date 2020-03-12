package com.minibank.inquiry.subscriber;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.minibank.inquiry.domain.entity.Customer;
import com.minibank.inquiry.exception.SystemException;
import com.minibank.inquiry.service.InquiryService;

@Component
public class CustomerConsumer {
	
	@Resource(name = "inquiryService")
    private InquiryService inquiryService;
	private final Logger LOGGER = LoggerFactory.getLogger(CustomerConsumer.class);
	
	/**
	 * 고객 데이터 생성 및 고객 이력 데이터 생성
	 * @param customer
	 * @throws SystemException
	 */
    @KafkaListener(topics = "${creating.customer.topic.name}", containerFactory = "customerKafkaListenerContainerFactory")
    public void creatingCustomerListener(Customer customer, Acknowledgment ack) {
        LOGGER.info("Recieved creating customer message: " + customer.getCstmId());
        try {
//        	/*고객 상세 조회 : 고객 데이터 등록*/
//        	inquiryService.createCustomer(customer);
//        	/*휴면 고객 목록 조회 : '고객등록' 작업 등록*/
//        	// CQRS 휴면계좌용 추가 라인
//        	inquiryService.updateCreatingCustomerWork(customer);
        	
        	inquiryService.createCustomerAndCustomerWork(customer);
          	ack.acknowledge();
          	
        } catch(Exception e) {
        	String msg = " 고객 데이터 생성 또는 고객 이력 생성에 문제가 발생했습니다.";
        	LOGGER.error(customer.getCstmId() + msg,e);
        } 
    }
    
    /**
     * 이체 정보를  업데이트 합니다.
     * @param customer
     * @throws SystemException
     */
    @KafkaListener(topics = "${updating.transfer.limit.topic.name}", containerFactory = "customerKafkaListenerContainerFactory")
    public void updatingTransferLimitListener(Customer customer, Acknowledgment ack) {
    	LOGGER.info("Recieved updating transfer limit message: " + customer.getCstmId());
        try {
        	inquiryService.updateTransferLimit(customer);
        	
           	ack.acknowledge();
        } catch(Exception e) {
        	String msg = " 이체 정보를 저장 중에 문제가 발생했습니다.";
        	LOGGER.error(customer.getCstmId() + msg,e);
        } 
    }
}
