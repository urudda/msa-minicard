package com.minibank.transfer.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.minibank.transfer.exception.SystemException;
import com.minibank.transfer.domain.entity.TransferHistory;
import com.minibank.transfer.domain.entity.TransferLimit;

@Component
public class TransferProducer {
	
	private final Logger LOGGER = LoggerFactory.getLogger(TransferProducer.class);
	
	@Autowired
    private KafkaTemplate<String, TransferHistory> transferKafkaTemplate;
    
	@Autowired
    private KafkaTemplate<String, TransferLimit> transferLimitKafkaTemplate;

    @Value(value = "${b2b.transfer.topic.name}")
    private String b2bTransferTopicName;
    
    @Value(value = "${updating.transfer.limit.topic.name}")
    private String updatingTransferLimitTopicName;
    
    @Value(value = "${transfer.topic.name}")
    private String transferTopicName;

    public void sendB2BTansferMessage(TransferHistory transfer) {
        ListenableFuture<SendResult<String, TransferHistory>> future = transferKafkaTemplate.send(b2bTransferTopicName, transfer);

        future.addCallback(new ListenableFutureCallback<SendResult<String, TransferHistory>>() {
            @Override
            public void onSuccess(SendResult<String, TransferHistory> result) {
                TransferHistory g = result.getProducerRecord().value();
                LOGGER.info("Sent message=[" + g.getCstmId() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                // needed to do compensation transaction.
                LOGGER.error( "Unable to send message=[" + transfer.getCstmId() + "] due to : " + ex.getMessage());
                throw new SystemException("Kafka 데이터 전송 에러");
            }
        });
    }
    
    public void sendUpdatingTansferLimitMessage(TransferLimit transferLimit) {
        ListenableFuture<SendResult<String, TransferLimit>> future = transferLimitKafkaTemplate.send(updatingTransferLimitTopicName, transferLimit);

        future.addCallback(new ListenableFutureCallback<SendResult<String, TransferLimit>>() {
            @Override
            public void onSuccess(SendResult<String, TransferLimit> result) {
                TransferLimit g = result.getProducerRecord().value();
                LOGGER.info("Sent message=[" + g.getCstmId() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                // needed to do compensation transaction.
                LOGGER.error( "Unable to send message=[" + transferLimit.getCstmId() + "] due to : " + ex.getMessage());
                throw new SystemException("Kafka 데이터 전송 에러");
            }
        });
    }
    
    public void sendCQRSTansferMessage(TransferHistory transfer) {
        ListenableFuture<SendResult<String, TransferHistory>> future = transferKafkaTemplate.send(transferTopicName, transfer);

        future.addCallback(new ListenableFutureCallback<SendResult<String, TransferHistory>>() {
            @Override
            public void onSuccess(SendResult<String, TransferHistory> result) {
                TransferHistory g = result.getProducerRecord().value();
                LOGGER.info("Sent message=[" + g.getCstmId() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                // needed to do compensation transaction.
                LOGGER.error( "Unable to send message=[" + transfer.getCstmId() + "] due to : " + ex.getMessage());
                throw new SystemException("Kafka 데이터 전송 에러");
            }
        });
    }
}
