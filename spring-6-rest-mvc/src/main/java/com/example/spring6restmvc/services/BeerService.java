package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    Boolean patchBeerById(UUID id, BeerDTO beerDTO);
    Boolean deleteBeerById(UUID id);
    Optional<BeerDTO> updateBeerById(UUID id, BeerDTO beerDTO);
    List<BeerDTO> listBeers();
    Optional<BeerDTO> getBeerById(UUID id);
    BeerDTO saveNewBeer(BeerDTO beerDTO);
}
