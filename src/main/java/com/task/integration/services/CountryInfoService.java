package com.task.integration.services;

import com.task.integration.clients.CountryInfoSoapClient;
import com.task.integration.entities.CountryInfo;
import com.task.integration.entities.Language;
import com.task.integration.exceptions.ResourceNotFoundException;
import com.task.integration.repositories.CountryInfoRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.oorsprong.websamples.ArrayOftLanguage;
import org.oorsprong.websamples.TCountryInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CountryInfoService {
    private static final Logger logger = LoggerFactory.getLogger(CountryInfoService.class);

    @Autowired
    private CountryInfoSoapClient soapClient;

    @Autowired
    private CountryInfoRepository countryInfoRepository;





    @Autowired
    public CountryInfoService(CountryInfoSoapClient countryInfoSoapClient) {
        this.soapClient = countryInfoSoapClient;
    }
    public CountryInfo createCountry(String countryName) {
        logger.info("Getting and saving country info for: {}", countryName);

        String capitalizedName = formatCountryName(countryName);
        String isoCode;
        TCountryInfo countryInfo;

        try {
            isoCode = soapClient.getCountryIsoCode(capitalizedName);
            if ("No country found by that name".equalsIgnoreCase(isoCode)) {
                throw new ResourceNotFoundException("No country found by that name: " + countryName);
            }
            countryInfo = soapClient.getFullCountryInfo(isoCode);
        } catch (Exception e) {
            logger.error("Error retrieving country information from SOAP service: {}", e.getMessage());
            throw new ResourceNotFoundException("Country information not found for: " + countryName + ". Error: " + e.getMessage());
        }

        if (countryInfo == null) {
            logger.warn("No country information retrieved for: {}", countryName);
            throw new ResourceNotFoundException("Country information not found for: " + countryName);
        }

        CountryInfo savedInfo = new CountryInfo();
        savedInfo.setIsoCode(countryInfo.getSISOCode());
        savedInfo.setName(countryInfo.getSName());
        savedInfo.setCapitalCity(countryInfo.getSCapitalCity());
        savedInfo.setPhoneCode(countryInfo.getSPhoneCode());
        savedInfo.setContinentCode(countryInfo.getSContinentCode());
        savedInfo.setCurrencyIsoCode(countryInfo.getSCurrencyISOCode());
        savedInfo.setCountryFlag(countryInfo.getSCountryFlag());

        // Add languages
        ArrayOftLanguage tLanguages = countryInfo.getLanguages();
        if (tLanguages != null && tLanguages.getTLanguage() != null) {
            List<Language> languages = tLanguages.getTLanguage().stream()
                    .map(tLanguage -> {
                        Language language = new Language();
                        language.setName(tLanguage.getSName());
                        language.setIsoCode(tLanguage.getSISOCode());
                        return language;
                    })
                    .collect(Collectors.toList());
            savedInfo.setLanguages(languages);
        }
        savedInfo = countryInfoRepository.save(savedInfo);

        logger.info("Saved country info for: {}", countryName);
        return savedInfo;
    }
    public List<CountryInfo> getAllCountries() {
        logger.info("Fetching all countries");
        List<CountryInfo> countries = countryInfoRepository.findAll();
        countries.forEach(country -> {
            country.getLanguages().size(); // Forces initialization if lazy loaded
        });
        return countries;
    }

    public CountryInfo getCountryById(Integer id) {
        logger.info("Fetching country with id: {}", id);
        Optional<CountryInfo> countryInfo = countryInfoRepository.findById(id);
        return countryInfo.orElse(null);
    }

    @Transactional
    public CountryInfo updateCountry(Integer id, CountryInfo countryInfo) {
        logger.info("Updating country with id: {}", id);
        CountryInfo existingCountry = countryInfoRepository.findById(id).orElse(null);
        if (existingCountry != null) {
            // Update basic properties
            BeanUtils.copyProperties(countryInfo, existingCountry, "id", "languages");

            // Update languages collection
            List<Language> updatedLanguages = countryInfo.getLanguages();
            existingCountry.getLanguages().clear(); // Remove all existing languages
            existingCountry.setId(id);
            existingCountry.setName(countryInfo.getName());
            if (updatedLanguages != null) {
                existingCountry.getLanguages().addAll(updatedLanguages); // Add updated languages
            }

            return countryInfoRepository.save(existingCountry);
        }
        throw new ResourceNotFoundException("Country not found with id: " + id);
    }
        public boolean deleteCountry(Integer id) {
        logger.info("Deleting country with id: {}", id);
        if (!countryInfoRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existent country with id: {}", id);
            throw new ResourceNotFoundException("Country not found with id: " + id);


        }
            countryInfoRepository.deleteById(id);
        logger.info("Deleted country with id: {}", id);
        return true;

    }
    private String formatCountryName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

}
