package com.minibank.inquiry.subscriber;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.minibank.inquiry.domain.entity.TransferHistory;
import com.minibank.inquiry.exception.SystemException;
import com.minibank.inquiry.service.InquiryService;

@Component
public class TransferConsumer {
	
	@Resource(name = "inquiryService")
    private InquiryService inquiryService;
	private final Logger LOGGER = LoggerFactory.getLogger(TransferConsumer.class);
	
	/**
	 *  타행 이체 정보 이력을 저장합니다.
	 * @param transfer
	 * @throws SystemException
	 */
    @KafkaListener(topics = "${transfer.topic.name}", containerFactory = "transferHistoryKafkaListenerContainerFactory")
    public void transferListener(TransferHistory transfer, Acknowledgment ack) {
    	LOGGER.info("Recieved transfer message: " + transfer.getCstmId());
    	
        try {
        	inquiryService.updateTransferWork(transfer);
        	ack.acknowledge();
        } catch(Exception e) {
        	String msg = " 타행이체 정보를 저장 중에 문제가 발생했습니다.";
        	LOGGER.error(transfer.getCstmId() + msg,e);
        } 
    }
}
