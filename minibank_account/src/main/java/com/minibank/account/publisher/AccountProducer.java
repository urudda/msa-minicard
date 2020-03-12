package com.minibank.account.publisher;

import com.minibank.account.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.minibank.account.domain.entity.Account;
import com.minibank.account.domain.entity.TransactionHistory;

@Component
public class AccountProducer {

    private final Logger LOGGER = LoggerFactory.getLogger(AccountProducer.class);
	
    @Autowired
    private KafkaTemplate<String, Account> accountKafkaTemplate;
    
    @Autowired
    private KafkaTemplate<String, TransactionHistory> transactionKafkaTemplate;
    
    @Value(value = "${creating.account.topic.name}")
    private String creatingAccountTopicName;
    
    @Value(value = "${updating.account.balance.topic.name}")
    private String updatingAccountBalanceTopicName;
    
    @Value(value = "${transaction.topic.name}")
    private String transactionTopicName;
    
    
    public void sendCreatingAccountMessage(Account account) {
        ListenableFuture<SendResult<String, Account>> future = accountKafkaTemplate.send(creatingAccountTopicName, account);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Account>>() {
            @Override
            public void onSuccess(SendResult<String, Account> result) {
                Account g = result.getProducerRecord().value();
                LOGGER.info("Sent message=[" + g.getAcntNo() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                // needed to do compensation transaction.
                LOGGER.error("Unable to send message=[" + account.getAcntNo() + "] due to : " + ex.getMessage());
                throw new SystemException("Kafka 데이터 전송 에러");
            }
        });
    }
    
    public void sendUpdatingAccountBalanceMessage(Account account) {
        ListenableFuture<SendResult<String, Account>> future = accountKafkaTemplate.send(updatingAccountBalanceTopicName, account);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Account>>() {
            @Override
            public void onSuccess(SendResult<String, Account> result) {
                Account g = result.getProducerRecord().value();
                LOGGER.info("Sent message=[" + g.getAcntNo() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                // needed to do compensation transaction.
                LOGGER.error("Unable to send message=[" + account.getAcntNo() + "] due to : " + ex.getMessage());
                throw new SystemException("Kafka 데이터 전송 에러");
            }
        });
    }
    
    public void sendTransactionMessage(TransactionHistory transaction) {
        ListenableFuture<SendResult<String, TransactionHistory>> future = transactionKafkaTemplate.send(transactionTopicName, transaction);

        future.addCallback(new ListenableFutureCallback<SendResult<String, TransactionHistory>>() {
            @Override
            public void onSuccess(SendResult<String, TransactionHistory> result) {
                TransactionHistory g = result.getProducerRecord().value();
                LOGGER.info("Sent message=[" + g.getAcntNo() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                // needed to do compensation transaction.
                LOGGER.error("Unable to send message=[" + transaction.getAcntNo() + "] due to : " + ex.getMessage());
                throw new SystemException("Kafka 데이터 전송 에러");
            }
        });
    }
}
