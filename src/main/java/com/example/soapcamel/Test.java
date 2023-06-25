package com.example.soapcamel;

import com.bft.springtarantoolapi.model.SmevMessageRecived;
import com.github.f4b6a3.uuid.UuidCreator;
import org.apache.camel.Body;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import ru.gov.pfr.ecp.iis.smev.adapter.core.common.types.SmevMethod;
import ru.gov.pfr.ecp.iis.smev.adapter.core.ea.dto.response.SingleDocumentOperationResponse;
import ru.gov.pfr.ecp.iis.smev.adapter.core.ea.dto.response.StoreResponse;
import ru.gov.pfr.ecp.iis.smev.adapter.core.ea.model.DaAttachment;
import ru.gov.pfr.ecp.iis.smev.adapter.core.ea.model.DaDocument;
import ru.gov.pfr.ecp.iis.smev.adapter.core.ea.model.SaveAttachmentInfo;
import ru.gov.pfr.ecp.iis.smev.adapter.core.ea.service.EAService;
import ru.gov.pfr.ecp.iis.smev.adapter.core.soapgate.model.SoapGateMessage;
import ru.gov.pfr.ecp.iis.smev.adapter.core.soapgate.model._1_2.types._1.SendRequestRequest;
import ru.gov.pfr.ecp.iis.smev.adapter.core.soapgate.model._1_2.types._1.SendRequestResponse;
import ru.gov.pfr.ecp.iis.smev.adapter.core.soapgate.model._1_2.types._1.SenderProvidedRequestData;
import ru.gov.pfr.ecp.iis.smev.adapter.core.soapgate.model._1_2.types.basic._1.MessagePrimaryContent;
import ru.gov.pfr.ecp.iis.smev.adapter.core.soapgate.model._1_2.types.basic._1.Void;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class Test {

    public static final String ENVELOP_XML_GUID_PROPERTY = "ENVELOP_XML_GUID";
    public static final String ROUTE_ID = "envelop-store";

    private String fileName;


    private final EAService eaService;

    public Test(EAService eaService) {
        this.eaService = eaService;
    }

    public SendRequestRequest request() throws JAXBException, ParserConfigurationException {

        SendRequestRequest sendRequestRequest = new SendRequestRequest();

        SenderProvidedRequestData senderProvidedRequestData = new SenderProvidedRequestData();

        String uuid = UuidCreator.getTimeBased().toString();

        System.out.println(uuid);

        senderProvidedRequestData.setMessageID(uuid);
        senderProvidedRequestData.setTestMessage(new Void());

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
        document.setXmlStandalone(false);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.marshal(request, document);

        MessagePrimaryContent messagePrimaryContent = new MessagePrimaryContent();

        messagePrimaryContent.setAny(document.getDocumentElement());

        senderProvidedRequestData.setMessagePrimaryContent(messagePrimaryContent);

        sendRequestRequest.setSenderProvidedRequestData(senderProvidedRequestData);

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
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        marshaller.marshal(sendRequestResponse, new File("./res.xml"));

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

    public void test(@Body SendRequestResponse sendRequestResponse){
        System.out.println("test");
    }

    public void envelop(@Body SoapGateMessage soapGateMessage){

        SoapGateMessage body = soapGateMessage;
        SmevMethod method = body.getMethod();
        if(method.equals(SmevMethod.SEND_REQUEST_REQUEST)){
            fileName = "SEND_REQUEST_REQUEST_" + body.getMessageId() + ".xml";
        } else if(method.equals(SmevMethod.SEND_REQUEST_RESPONSE)){
            fileName = "SEND_REQUEST_RESPONSE_" + body.getMessageId() + ".xml";
        }
        System.out.println(fileName);
//        StoreResponse file = eaService.storeEnvelop(
//                body.getMessageId(),
//                exchange.getProperty(SmevRequestHandler.ORIGINAL_CONTENT_PROPERTY, String.class).getBytes(StandardCharsets.UTF_8), fileName);
//
//        List<SaveAttachmentInfo> attachments = List.of(
//                SaveAttachmentInfo.builder().fileGuid(file.getFileGuid()).build()
//        );
//
//        SingleDocumentOperationResponse response = eaService.saveDocument(body, attachments);
//        DaAttachment envelop = Optional.ofNullable(response.getDocument())
//                .map(DaDocument::getAttachments)
//                .filter(l -> !l.isEmpty())
//                .map(l -> l.get(0))
//                .orElseThrow(() -> new ArrayStoreException("There is no saved files in response"));
//
//        exchange.setProperty(ENVELOP_XML_GUID_PROPERTY, envelop.getFileGuid());
//        System.out.println("Xml Envelop Body for messageId = " + body.getMessageId() + " has been successfully stored. Envelop GUID: " + envelop.getFileGuid());
    }

}
