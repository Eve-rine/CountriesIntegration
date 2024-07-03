package com.task.integration.entities;

import jakarta.persistence.*;

@Entity
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String isoCode;
    private String name;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    // Getters
    public Long getId() {
        return id;
    }
    public String getIsoCode() {
        return isoCode;
    }

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryInfo country;
}
