package com.example.soapcamel;

import com.bft.springtarantoolapi.model.SmevMessageRecived;
import com.bft.springtarantoolapi.service.SmevMessageRecivedService;
import org.apache.camel.Body;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SendTarantoolService {
    private final SmevMessageRecivedService smevMessageRecivedService;

    public SendTarantoolService(SmevMessageRecivedService smevMessageRecivedService) {
        this.smevMessageRecivedService = smevMessageRecivedService;
    }

    public void saveSMEVMessage(@Body SmevMessageRecived smevMessageRecived) {
        smevMessageRecivedService.saveSMEVMessage(smevMessageRecived);
    }
}
