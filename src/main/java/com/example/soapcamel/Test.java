package com.example.soapcamel;

import com.bft.springtarantoolapi.model.SmevMessageRecived;
import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.helpers.IOUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import ru.gov.pfr.ecp.iis.smev.adapter.core.common.entity.ReceiverConfigurationEntity;
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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class Test {

    private final EAService eaService;

    private String uuid;


    public SendRequestRequest request() throws JAXBException, ParserConfigurationException {

        SendRequestRequest sendRequestRequest = new SendRequestRequest();

        SenderProvidedRequestData senderProvidedRequestData = new SenderProvidedRequestData();

        uuid = UuidCreator.getTimeBased().toString();

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

    public SmevMessageRecived response(@Body SendRequestResponse sendRequestResponse) throws JAXBException, IOException {

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

    public void storeEnvelop(Exchange exchange) throws JAXBException, IOException{
        String contents =  extractSoapEnvelopHeader(exchange);

        new File("src/main/resources/"+uuid).mkdirs();

        Path path = Paths.get("src/main/resources/"+uuid+"/output.txt");

        Files.writeString(path, contents, StandardCharsets.UTF_8);

        File output = new File("src/main/resources/"+uuid+"/output.txt");

        eaService.storeEnvelop(uuid, contents.getBytes(StandardCharsets.UTF_8), "SEND_REQUEST_RESPONSE_" + uuid + ".xml");
    }

    public String extractSoapEnvelopHeader(Exchange exchange) throws IOException {
        Map<String, Object> headers = exchange.getIn().getHeaders();
        SoapMessage soapMessage = (SoapMessage) headers.get("CamelCxfMessage");

        try (InputStream in = (ByteArrayInputStream) soapMessage.get("SoapEnvelop")) {
            byte[] payload = IOUtils.readBytesFromStream(in);

            return new String(payload, StandardCharsets.UTF_8);
        }
    }

}
