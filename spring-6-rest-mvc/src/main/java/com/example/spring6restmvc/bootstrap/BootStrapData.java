package com.example.spring6restmvc.bootstrap;

import com.example.spring6restmvc.entities.Beer;
import com.example.spring6restmvc.model.BeerStyle;
import com.example.spring6restmvc.entities.Customer;
import com.example.spring6restmvc.repositories.BeerRepository;
import com.example.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner {
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
    }

    private void loadBeerData() {
        if(beerRepository.count() == 0) {
            Beer b1 = Beer.builder()
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
            Beer b2 = Beer.builder()
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

            Beer b3 = Beer.builder()
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

            beerRepository.save(b1);
            beerRepository.save(b2);
            beerRepository.save(b3);
        }
    }

    private void loadCustomerData() {
        if(customerRepository.count() == 0) {
            Customer c1 = Customer.builder()
                    .id(UUID.randomUUID())
                    .customerName("Ananth")
                    .version(100)
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();
            Customer c2 = Customer.builder()
                    .id(UUID.randomUUID())
                    .customerName("Saranya")
                    .version(100)
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();
            Customer c3 = Customer.builder()
                    .id(UUID.randomUUID())
                    .customerName("Ananya")
                    .version(100)
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();
            Customer c4 = Customer.builder()
                    .id(UUID.randomUUID())
                    .customerName("Aryan")
                    .version(100)
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();
            customerRepository.saveAll(Arrays.asList(c1,c2,c3,c4));
        }

    }
}
