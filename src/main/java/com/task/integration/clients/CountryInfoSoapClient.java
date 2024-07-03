//

package com.task.integration.clients;

import jakarta.annotation.PostConstruct;
import org.oorsprong.websamples.CountryInfoService;
import org.oorsprong.websamples.CountryInfoServiceSoapType;
import org.oorsprong.websamples.TCountryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import java.net.URL;

@Service
public class CountryInfoSoapClient {
    private static final Logger logger = LoggerFactory.getLogger(CountryInfoSoapClient.class);

    private static final String WSDL_URL = "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL";
    private static final String NAMESPACE_URI = "http://www.oorsprong.org/websamples.countryinfo";
    private static final String LOCAL_PART = "CountryInfoService";

    private CountryInfoServiceSoapType countryInfoService;

    @PostConstruct
    public void init() {
        logger.info("Initializing CountryInfoSoapClient...");
        try {
            URL url = new URL(WSDL_URL);
            logger.info("WSDL URL created: {}", url);

            QName qname = new QName(NAMESPACE_URI, LOCAL_PART);
            logger.info("QName created: {}", qname);

            CountryInfoService service = new CountryInfoService(url, qname);
            logger.info("CountryInfoService created");

            countryInfoService = service.getCountryInfoServiceSoap();
            logger.info("CountryInfoServiceSoap obtained");

            logger.info("CountryInfoService initialized successfully.");
        } catch (Exception e) {
            logger.error("Failed to initialize CountryInfoService", e);
            throw new RuntimeException("Failed to initialize CountryInfoService", e);
        }
    }

    public String getCountryIsoCode(String countryName) {
        if (countryInfoService == null) {
            throw new IllegalStateException("countryInfoService is not initialized");
        }
        return countryInfoService.countryISOCode(countryName);
    }

    public TCountryInfo getFullCountryInfo(String isoCode) {
        if (countryInfoService == null) {
            throw new IllegalStateException("countryInfoService is not initialized");
        }
        return countryInfoService.fullCountryInfo(isoCode);
    }
}