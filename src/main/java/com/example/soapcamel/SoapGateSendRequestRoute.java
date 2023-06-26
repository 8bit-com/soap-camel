package com.example.soapcamel;

import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import ru.gov.pfr.ecp.iis.smev.adapter.core.common.entity.ReceiverConfigurationEntity;
import javax.xml.bind.JAXBException;

@Component
@RequiredArgsConstructor
public class SoapGateSendRequestRoute extends RouteBuilder {

    private final SmevRequestHandlerSupplier handlerSupplier;

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
                .bean(Test.class, "storeEnvelop");


                //.bean(Test.class, "response")
                //.bean(SendTarantoolService.class, "saveSMEVMessage");
    }


}