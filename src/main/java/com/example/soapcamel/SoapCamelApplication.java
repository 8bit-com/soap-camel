package com.example.soapcamel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.gov.pfr.ecp.iis.smev.adapter.core.common.configuration.SmevAdapterCoreJpaRepositories;

@SpringBootApplication
@SmevAdapterCoreJpaRepositories
public class SoapCamelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoapCamelApplication.class, args);
    }

}
