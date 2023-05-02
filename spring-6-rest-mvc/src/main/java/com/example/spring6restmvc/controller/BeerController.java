package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.model.BeerDTO;
import com.example.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@RestController
public class BeerController {
    public static final String BEER_PATH = "/api/vi/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
    private final BeerService beerService;

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity patchBeerById(@PathVariable("beerId") UUID beerId,@Validated @RequestBody BeerDTO beerDTO) {
        if(!beerService.patchBeerById(beerId, beerDTO)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteBeerById(@PathVariable("beerId") UUID beerId) {
        if(!beerService.deleteBeerById(beerId)) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId,@Validated @RequestBody BeerDTO beerDTO) {
        if(beerService.updateBeerById(beerId, beerDTO).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity saveNewBeer(@Validated @RequestBody BeerDTO beerDTO) {
        BeerDTO b = beerService.saveNewBeer(beerDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH + "/" + b.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @GetMapping(BEER_PATH)
    public List<BeerDTO> listBeers() {
        return beerService.listBeers();
    }
}
