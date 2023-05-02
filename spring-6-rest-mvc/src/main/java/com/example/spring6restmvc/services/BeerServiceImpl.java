package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.BeerDTO;
import com.example.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {

    public Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        beerMap = new HashMap<>();
        BeerDTO b1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("Blue Moon")
                .beerStyle(BeerStyle.WHEAT)
                .upc("12345")
                .price(new BigDecimal(10.99))
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .quantityOnHand(100)
                .version(1)
                .build();
        BeerDTO b2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("Michelob Ultra")
                .beerStyle(BeerStyle.LAGER)
                .upc("12346")
                .price(new BigDecimal(8.99))
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .quantityOnHand(110)
                .version(1)
                .build();

        BeerDTO b3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("Guinness")
                .beerStyle(BeerStyle.STOUT)
                .upc("12345")
                .price(new BigDecimal(13.99))
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .quantityOnHand(120)
                .version(1)
                .build();

        beerMap.put(b1.getId(), b1);
        beerMap.put(b2.getId(), b2);
        beerMap.put(b3.getId(), b3);
    }
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("Enter the getBeerById in service");
        return Optional.of(beerMap.get(id));
    }

    @Override
    public List<BeerDTO> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        BeerDTO b = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName(beerDTO.getBeerName())
                .beerStyle(beerDTO.getBeerStyle())
                .upc(beerDTO.getUpc())
                .price(beerDTO.getPrice())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .quantityOnHand(beerDTO.getQuantityOnHand())
                .version(1)
                .build();
        beerMap.put(b.getId(), b);
        return b;
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beerDTO) {
        BeerDTO existing = beerMap.get(beerId);
        existing.setBeerName(beerDTO.getBeerName());
        existing.setQuantityOnHand(beerDTO.getQuantityOnHand());
        existing.setPrice(beerDTO.getPrice());
        return Optional.of(existing);
    }

    @Override
    public Boolean deleteBeerById(UUID beerId) {
        beerMap.remove(beerId);
        return true;
    }

    @Override
    public Boolean patchBeerById(UUID id, BeerDTO beerDTO) {
        BeerDTO existing = beerMap.get(id);
        if(StringUtils.hasText(beerDTO.getBeerName())) {
            existing.setBeerName(beerDTO.getBeerName());
        }
        if(beerDTO.getQuantityOnHand() != null) {
            existing.setQuantityOnHand(beerDTO.getQuantityOnHand());
        }
        if(beerDTO.getPrice() != null) {
            existing.setPrice(beerDTO.getPrice());
        }
        return true;
    }
}
