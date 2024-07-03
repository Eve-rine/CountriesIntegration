package com.task.integration.controllers;


import com.task.integration.entities.CountryInfo;
import com.task.integration.exceptions.ResourceNotFoundException;
import com.task.integration.services.CountryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

    private final CountryInfoService countryService;

    @Autowired
    public CountryController(CountryInfoService countryService) {
        this.countryService = countryService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> getCountryIsoCode(@RequestBody Map<String, String> request) {
        String countryName = request.get("name");
        if (countryName == null || countryName.isEmpty()) {
            return ResponseEntity.badRequest().body("Country name is required.");
        }

        countryName = countryName.substring(0, 1).toUpperCase() + countryName.substring(1).toLowerCase();

        CountryInfo countryInfo = countryService.createCountry(countryName);
        return new ResponseEntity<>(HttpStatus.CREATED);
        }


    // Fetch all country information
    @GetMapping
    public ResponseEntity<List<CountryInfo>> getAllCountries() {
        List<CountryInfo> countries = countryService.getAllCountries();
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    // Fetch country information by ID
    @GetMapping("/{id}")
    public ResponseEntity<CountryInfo> getCountryById(@PathVariable Integer id) {
        CountryInfo country = countryService.getCountryById(id);
        if (country != null) {
            return new ResponseEntity<>(country, HttpStatus.OK);
        } else {
            // return 404 if not found and a message
            throw new ResourceNotFoundException("Country not found with id: " + id);


        }
    }

    // Update country information
    @PutMapping("/{id}")
    public ResponseEntity<CountryInfo> updateCountry(@PathVariable Integer id, @RequestBody CountryInfo countryInfo) {
        CountryInfo updatedCountry = countryService.updateCountry(id, countryInfo);
        if (updatedCountry != null) {
            return new ResponseEntity<>(updatedCountry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCountry(@PathVariable Integer id) {
        if (countryService.deleteCountry(id)) {
            return ResponseEntity.ok("Country deleted successfully");
        } else {
            throw new ResourceNotFoundException("Country not found with id: " + id);

        }
    }
}

