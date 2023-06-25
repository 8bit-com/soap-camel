package com.example.soapcamel;

import org.apache.camel.Exchange;
import ru.gov.pfr.ecp.iis.smev.adapter.core.common.entity.ReceiverConfigurationEntity;
import ru.gov.pfr.ecp.iis.smev.adapter.core.common.types.SmevMethod;
import ru.gov.pfr.ecp.iis.smev.adapter.core.common.types.SmevVersion;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

public interface SmevRequestHandler<Request, Response> {

    String ORIGINAL_CONTENT_PROPERTY = "OriginalContent";

    Request request(ReceiverConfigurationEntity configuration) throws DatatypeConfigurationException, JAXBException;

    Response response(Exchange body) throws JAXBException;

    SmevVersion getVersion();

    SmevMethod getMethod();
}
