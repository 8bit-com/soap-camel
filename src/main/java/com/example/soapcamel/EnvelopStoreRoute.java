package com.example.soapcamel;

import lombok.RequiredArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gov.pfr.ecp.iis.smev.adapter.core.common.types.SmevMethod;
import ru.gov.pfr.ecp.iis.smev.adapter.core.ea.dto.response.SingleDocumentOperationResponse;
import ru.gov.pfr.ecp.iis.smev.adapter.core.ea.dto.response.StoreResponse;
import ru.gov.pfr.ecp.iis.smev.adapter.core.ea.model.DaAttachment;
import ru.gov.pfr.ecp.iis.smev.adapter.core.ea.model.DaDocument;
import ru.gov.pfr.ecp.iis.smev.adapter.core.ea.model.SaveAttachmentInfo;
import ru.gov.pfr.ecp.iis.smev.adapter.core.ea.service.EAService;
import ru.gov.pfr.ecp.iis.smev.adapter.core.soapgate.model.SoapGateMessage;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EnvelopStoreRoute extends RouteBuilder {

    public static final String ENVELOP_XML_GUID_PROPERTY = "ENVELOP_XML_GUID";
    public static final String ROUTE_ID = "envelop-store";

    private String fileName;

    @Autowired
    private final EAService eaService;

    @Override
    public void configure() throws Exception {
        onException(ArrayStoreException.class)
                .handled(true)
                .logStackTrace(true)
                .log(LoggingLevel.ERROR, "Error occurred during envelop store attempt: ${exception}")
                .log(LoggingLevel.ERROR, "${exception.message}")
                .log(LoggingLevel.ERROR, "${exception.stacktrace}")
                .stop();

        from("direct:" + ROUTE_ID)
                .id(ROUTE_ID)
                .log("Trying to store xml-response body via EAMediator...")
                .process(exchange -> {
                    SoapGateMessage body = exchange.getIn().getBody(SoapGateMessage.class);
                    SmevMethod method = body.getMethod();
                    if(method.equals(SmevMethod.SEND_REQUEST_REQUEST)){
                        fileName = "SEND_REQUEST_REQUEST_" + body.getMessageId() + ".xml";
                    } else if(method.equals(SmevMethod.SEND_REQUEST_RESPONSE)){
                        fileName = "SEND_REQUEST_RESPONSE_" + body.getMessageId() + ".xml";
                    }
                    StoreResponse file = eaService.storeEnvelop(
                            body.getMessageId(),
                            exchange.getProperty(SmevRequestHandler.ORIGINAL_CONTENT_PROPERTY, String.class).getBytes(StandardCharsets.UTF_8), fileName);

                    List<SaveAttachmentInfo> attachments = List.of(
                            SaveAttachmentInfo.builder().fileGuid(file.getFileGuid()).build()
                    );

                    SingleDocumentOperationResponse response = eaService.saveDocument(body, attachments);
                    DaAttachment envelop = Optional.ofNullable(response.getDocument())
                            .map(DaDocument::getAttachments)
                            .filter(l -> !l.isEmpty())
                            .map(l -> l.get(0))
                            .orElseThrow(() -> new ArrayStoreException("There is no saved files in response"));

                    exchange.setProperty(ENVELOP_XML_GUID_PROPERTY, envelop.getFileGuid());
                    log.info("Xml Envelop Body for messageId = " + body.getMessageId() + " has been successfully stored. Envelop GUID: " + envelop.getFileGuid());
                });
    }
}
