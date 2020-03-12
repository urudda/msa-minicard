package com.minibank.inquiry.subscriber;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.minibank.inquiry.domain.entity.Account;
import com.minibank.inquiry.domain.entity.TransactionHistory;
import com.minibank.inquiry.exception.SystemException;
import com.minibank.inquiry.service.InquiryService;

@Component
public class AccountConsumer {
	@Resource(name = "inquiryService")
    private InquiryService inquiryService;
	private final Logger LOGGER = LoggerFactory.getLogger(AccountConsumer.class);

	
    @KafkaListener(topics = "${creating.account.topic.name}", containerFactory = "accountKafkaListenerContainerFactory")
    public void creatingAccountListener(Account account, Acknowledgment ack) {
    	LOGGER.info("Recieved creating account message: " + account.getAcntNo());
  
        try {
//        	/*고객상세조회 : 계좌 데이터 등록*/
//        	inquiryService.createAccount(account);
//        	/*휴면고객목록조회 : '계좌등록'작업 등록*/
//        	// CQRS 휴면계좌 용 추가 라인
//        	inquiryService.updateCreatingAccountWork(account);
        	inquiryService.createAccountAndUpdateAccountWork(account);
        	
           	ack.acknowledge();  // 성공시 커밋
           	
        } catch(Exception e) {
        	String msg = " 계좌 데이터 또는 계좌 이력 데이터 등록에 문제가 발생했습니다.";
        	LOGGER.error(account.getAcntNo() + msg,e);
        } 
    }
    
    /**
     * 계좌 잔액 업데이트
     * @param account
     * @throws SystemException
     */
    @KafkaListener(topics = "${updating.account.balance.topic.name}", containerFactory = "accountKafkaListenerContainerFactory")
    public void updatingAccountBalanceListener(Account account, Acknowledgment ack) {
    	LOGGER.info("Recieved updating account balance account message: " + account.getAcntNo());
    	
        try {
        	inquiryService.updateAccountBalance(account);
          	ack.acknowledge();
        }catch(Exception e) {
        	String msg = " 계좌 잔액 업데이트에 문제가 발생했습니다.";
        	LOGGER.error(account.getAcntNo() + msg,e);
        } 
    }
    
    /**
     * 이체 정보 이력을 저장
     * @param transaction
     * @throws SystemException
     */
    @KafkaListener(topics = "${transaction.topic.name}", containerFactory = "transactionHistoryKafkaListenerContainerFactory")
    public void transactionListener(TransactionHistory transaction, Acknowledgment ack) {
        LOGGER.info("Recieved transaction message: " + transaction.getAcntNo());
        
        try {
        	inquiryService.updateTransactionWork(transaction);
           	ack.acknowledge(); // 성공시 커밋
        }catch(Exception e) {
        	String msg = " 이체 정보 이력 저장에 문제가 발생했습니다.";
        	LOGGER.error(transaction.getAcntNo() + msg,e);
//        	ack.nack(1000 * 3); // 실패시 3초 후 재 실행, 지정하지 않으면 KAFKA의 기본 값 
        } 
    }
}
