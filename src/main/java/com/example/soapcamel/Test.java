package com.example.soapcamel;

import com.bft.springtarantoolapi.model.SmevMessageRecived;
import com.fasterxml.uuid.Generators;
import org.apache.camel.Body;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ru.gov.pfr.ecp.iis.smev.adapter.core.soapgate.model._1_2.types._1.SendRequestRequest;
import ru.gov.pfr.ecp.iis.smev.adapter.core.soapgate.model._1_2.types._1.SendRequestResponse;
import ru.gov.pfr.ecp.iis.smev.adapter.core.soapgate.model._1_2.types._1.SenderProvidedRequestData;
import ru.gov.pfr.ecp.iis.smev.adapter.core.soapgate.model._1_2.types.basic._1.MessagePrimaryContent;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.print.Book;
import java.io.File;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class Test {

    public SendRequestRequest request() throws JAXBException, ParserConfigurationException {

        UUID uuid = Generators.timeBasedGenerator().generate();

        System.out.println(uuid.toString());

        SendRequestRequest sendRequestRequest = new SendRequestRequest();

        SenderProvidedRequestData senderProvidedRequestData = new SenderProvidedRequestData();

        senderProvidedRequestData.setMessageID(uuid.toString());

        Request request = Request.builder()
                .idOrder("4564654645")
                .dateOrder("2021-02-18")
                .FIO(new FIODto("Иванов", "Петр", "Сергеевич"))
                .SNILS("82095352437")
                .birthday("1984-12-05")
                .gender("male")
                .period(new Period("2021-01-01", "2021-01-31"))
                .build();

        JAXBContext jc = JAXBContext.newInstance(Request.class);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Marshaller marshaller = jc.createMarshaller();
        marshaller.marshal(request, document);

        MessagePrimaryContent messagePrimaryContent = new MessagePrimaryContent();

        messagePrimaryContent.setAny(document.getDocumentElement());

        senderProvidedRequestData.setMessagePrimaryContent(messagePrimaryContent);

        sendRequestRequest.setSenderProvidedRequestData(senderProvidedRequestData);

        JAXBContext context = JAXBContext.newInstance(SendRequestRequest.class);
        Marshaller mar= context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(sendRequestRequest, new File("./req.xml"));

        return sendRequestRequest;
    };

    public SmevMessageRecived response(@Body SendRequestResponse sendRequestResponse) throws JAXBException {

        System.out.println(sendRequestResponse.getMessageMetadata().getMessageId());
        System.out.println(sendRequestResponse.getMessageMetadata().getSendingTimestamp());
        System.out.println(sendRequestResponse.getMessageMetadata().getMessageType());
        System.out.println(sendRequestResponse.getMessageMetadata().getId());
        System.out.println(sendRequestResponse.getMessageMetadata().getRecipient());
        System.out.println(sendRequestResponse.getMessageMetadata().getSender());
        System.out.println(sendRequestResponse.getMessageMetadata().getStatus());
        System.out.println(sendRequestResponse.getSMEVSignature().getAny());

        JAXBContext context = JAXBContext.newInstance(SendRequestResponse.class);
        Marshaller mar= context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(sendRequestResponse, new File("./res.xml"));

        return SmevMessageRecived.builder()
                .messageId(sendRequestResponse.getMessageMetadata().getMessageId())
                .proxyId(1)
                .smevXmlGuid("smevXmlGuid")
                .attachmentGuids(new String[]{"1", "2"})
                .deliveryTimeStamp(ZonedDateTime.parse(sendRequestResponse.getMessageMetadata().getSendingTimestamp().toString()))
                .processingTopicName("processingTopicName") // топик получаем из базы для определенного вида взаимодейтвия
                .smevMessageType(0)
                .build();
    };

}
