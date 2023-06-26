package com.example.soapcamel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gov.pfr.ecp.iis.smev.adapter.core.common.entity.ReceiverConfigurationEntity;
import ru.gov.pfr.ecp.iis.smev.adapter.core.common.types.SmevMethod;
import ru.gov.pfr.ecp.iis.smev.adapter.core.common.types.SmevVersion;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SmevRequestHandlerSupplier {

    private final Map<String, SmevRequestHandler> requestHandlerMap;

    public SmevRequestHandlerSupplier(@Autowired List<SmevRequestHandler> requestHandlerList) {
        requestHandlerMap = requestHandlerList.stream()
                .collect(Collectors.toMap(
                        h -> buildHandlerKey(h.getMethod(), h.getVersion()),
                        h -> h));
    }

    public SmevRequestHandler getRequestHandler(ReceiverConfigurationEntity configuration) {
        SmevVersion smevVersion = SmevVersion.fromOrdinal(configuration.getProxy().getVersion().getKey());
        SmevMethod smevMethod = SmevMethod.fromOrdinal(configuration.getSmevMethod().getKey());
        String key = buildHandlerKey(smevMethod, smevVersion);

        return requestHandlerMap.get(key);
    }

    private String buildHandlerKey(SmevMethod methodType, SmevVersion smevVersion) {
        return methodType.name() + smevVersion.name();
    }


}
