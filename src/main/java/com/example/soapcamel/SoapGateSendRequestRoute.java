package com.example.soapcamel;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import javax.xml.bind.UnmarshalException;
import java.util.Objects;

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
                .setHeader(CxfConstants.OPERATION_NAME, constant("SendRequest"))
                .setHeader("Content-Type", constant("application/soap+xml"))
                .to("cxf://" + "http://172.18.32.61:80/g2g/smev_proxy" +
                        "?serviceClass=ru.gov.pfr.ecp.iis.smev.adapter.core.soapgate.model._1_2.SMEVMessageExchangePortType" +
                        "&loggingFeatureEnabled=true")
                .bean(Test.class, "response")
                .bean(SendTarantoolService.class, "saveSMEVMessage");
    }

}