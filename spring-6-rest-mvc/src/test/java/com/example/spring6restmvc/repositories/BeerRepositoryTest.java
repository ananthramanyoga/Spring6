package com.example.spring6restmvc.repositories;

import com.example.spring6restmvc.entities.Beer;
import com.example.spring6restmvc.model.BeerStyle;
import com.example.spring6restmvc.repositories.BeerRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveNewBeerTooLong() {

        assertThrows(ConstraintViolationException.class, () -> {
            Beer savedBeer = beerRepository.save(Beer.builder()
                    .beerName("New Beerasdadsadadadsadewrwefwdsfasdaffsgtewrfasdfwedsdfsdfewsdfsfqasefsdfsdfgjhgjgjdgdh")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("1234567")
                    .price(new BigDecimal(9.99))
                    .build()
            );
            beerRepository.flush();
        });
    }

    @Test
    void testSaveNewBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("New Beer")
                .beerStyle(BeerStyle.LAGER)
                .upc("1234567")
                .price(new BigDecimal(9.99))
                .build()
        );
        beerRepository.flush();
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }
}