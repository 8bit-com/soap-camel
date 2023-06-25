package com.example.soapcamel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import ru.gov.pfr.ecp.iis.smev.adapter.core.soap.util.SoapMessageUtil;
import ru.gov.pfr.ecp.iis.smev.adapter.core.soapgate.model._1_2.types._1.SendRequestRequest;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

@Component
public class SoapGateSendRequestRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //настройка rest
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        //rest
        rest("/send_req")
                .get()
                .to("direct:start");

        from("direct:start")
                .routeId("etalon")
                .bean(Test.class, "request")
                .removeHeaders("*")
                .setHeader(CxfConstants.OPERATION_NAME, constant("SendRequest"))
                .setHeader("Accept", constant("application/soap+xml"))
                .setHeader("Host", constant("172.18.32.61:80"))
                .setHeader("Content-Type", constant("application/soap+xml"))
                .to("cxf:bean:ru.gov.pfr.ecp.iis.smev.adapter.core.soapgate.model._1_2.SMEVMessageExchangePortType")
                //.bean(Test.class, "test")
                //.bean(Test.class, "envelop")
                //.to("direct:" + EnvelopStoreRoute.ROUTE_ID)

                .bean(Test.class, "response")
                .bean(SendTarantoolService.class, "saveSMEVMessage");
    }

}