package com.minibank.transfer.subscriber;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.minibank.transfer.domain.entity.TransferHistory;
import com.minibank.transfer.exception.SystemException;
import com.minibank.transfer.publisher.TransferProducer;
import com.minibank.transfer.rest.account.AccountFeignClient;
import com.minibank.transfer.rest.account.entity.TransactionHistory;
import com.minibank.transfer.service.TransferService;

@Component
public class B2BTransferResultConsumer {
    
	private final Logger LOGGER = LoggerFactory.getLogger("B2BTransferResultConsumer.class");
	
	@Resource(name = "transferService")
    private TransferService transferService;
	
    @Autowired
    TransferProducer transferProducer;
	
    @Autowired
    AccountFeignClient accountFeignClient;
	
    @KafkaListener(topics = "${b2b.transfer.result.topic.name}", containerFactory = "b2bTransferResultKafkaListenerContainerFactory")
    public void b2bTransferResultListener(TransferHistory transferResult, Acknowledgment ack) throws Exception {
    	LOGGER.info("Recieved Bank-To-Bank transfer result message: " + transferResult.getWthdAcntNo());
        
        String statusCode = transferResult.getStsCd();
        
        try {
        	if("2".equals(statusCode)) {
            	String wthdAcntNo = transferResult.getWthdAcntNo();
            	int wthdAcntSeq = transferResult.getWthdAcntSeq();

            	accountFeignClient.cancelWithdraw(
            			TransactionHistory.builder()
											.acntNo(wthdAcntNo)
											.seq(wthdAcntSeq)
											.build()
            			);
            }
            
            transferService.createTransferHistory(transferResult);
            
            /*CQRS*/
            transferProducer.sendCQRSTansferMessage(transferResult);
            
          	ack.acknowledge(); // 모든 CRUD 작업이 완료되어야만 kafka의 read off-set 값을 변경하도록 합니다.
        } catch (Exception e) {
        	String msg = "시스템에 예상치 못한 문제가 발생했습니다";
        	LOGGER.error(msg, e);
            throw new SystemException(msg);
        } 
    }
}
