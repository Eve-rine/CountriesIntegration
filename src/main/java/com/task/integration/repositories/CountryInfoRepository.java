package com.task.integration.repositories;

import com.task.integration.entities.CountryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryInfoRepository extends JpaRepository<CountryInfo, Integer> {

}
