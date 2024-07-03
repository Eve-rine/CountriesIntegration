package com.task.integration.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class CountryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String isoCode;
    private String name;
    private String capitalCity;
    private String phoneCode;
    private String continentCode;
    private String currencyIsoCode;
    private String countryFlag;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "country_id")

    private List<Language> languages;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String sName) {
        this.name = sName;
    }

    public void setIsoCode(String sisoCode) {
        this.isoCode = sisoCode;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public String getName() {
        return name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public String getContinentCode() {
        return continentCode;
    }

    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public void setCapitalCity(String sCapitalCity) {
        this.capitalCity = sCapitalCity;
    }

    public void setPhoneCode(String sPhoneCode) {
        this.phoneCode = sPhoneCode;
    }

    public void setContinentCode(String sContinentCode) {
        this.continentCode = sContinentCode;
    }

    public void setCurrencyIsoCode(String sCurrencyISOCode) {
        this.currencyIsoCode = sCurrencyISOCode;
    }

    public void setCountryFlag(String sCountryFlag) {
        this.countryFlag = sCountryFlag;
    }
}

