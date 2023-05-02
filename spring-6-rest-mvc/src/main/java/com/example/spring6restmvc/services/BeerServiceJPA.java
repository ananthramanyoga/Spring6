package com.example.spring6restmvc.services;

import com.example.spring6restmvc.mappers.BeerMapper;
import com.example.spring6restmvc.model.BeerDTO;
import com.example.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    @Autowired
    private final BeerRepository beerRepository;
    @Autowired
    private final BeerMapper beerMapper;
    @Override
    public Boolean patchBeerById(UUID id, BeerDTO beerDTO) {
        AtomicBoolean flag = new AtomicBoolean(true);
        beerRepository.findById(id).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beerDTO.getBeerName());
            foundBeer.setPrice(beerDTO.getPrice());
            foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
            beerRepository.save(foundBeer);
        }, () -> {
            flag.set(false);
        } );
        return flag.get();
    }

    @Override
    public Boolean deleteBeerById(UUID id) {
        if(!beerRepository.findById(id).isEmpty()) {
            beerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID id, BeerDTO beerDTO) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();
        beerRepository.findById(id).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beerDTO.getBeerName());
            foundBeer.setBeerStyle(beerDTO.getBeerStyle());
            foundBeer.setUpc(beerDTO.getUpc());
            foundBeer.setPrice(beerDTO.getPrice());
            atomicReference.set(Optional.of(
                    beerMapper.beerToBeerDto(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public List<BeerDTO> listBeers() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDto
                (beerRepository.findById(id).orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDTO)));
    }
}
