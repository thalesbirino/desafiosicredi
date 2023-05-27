package com.sicredi.desafio.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@Slf4j
public class SqsService {

    private final SqsClient sqsClient;
    private final String queueUrl;

    @Autowired
    public SqsService(SqsClient sqsClient, @Value("${aws.sqs.queue-url}") String queueUrl) {
        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
    }

    public void sendMessage(String messageBody) {
        log.info("Enviando mensagem para a fila SQS: {}", messageBody);

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();

        sqsClient.sendMessage(request);
    }

    public ReceiveMessageResponse receiveMessages() {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .build();

        log.info("Recebendo mensagens da fila SQS");

        return sqsClient.receiveMessage(request);
    }

    public void deleteMessage(String receiptHandle) {
        DeleteMessageRequest request = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(receiptHandle)
                .build();

        log.info("Excluindo mensagem da fila SQS com receipt handle: {}", receiptHandle);

        sqsClient.deleteMessage(request);
    }
}
