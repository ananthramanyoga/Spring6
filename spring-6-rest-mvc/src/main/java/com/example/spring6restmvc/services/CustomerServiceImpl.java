package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        customerMap = new HashMap<>();
        CustomerDTO c1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Ananth")
                .version(100)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        CustomerDTO c2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Saranya")
                .version(100)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        CustomerDTO c3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Ananya")
                .version(100)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        CustomerDTO c4 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Aryan")
                .version(100)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        customerMap.put(c1.getId(),c1);
        customerMap.put(c2.getId(),c2);
        customerMap.put(c3.getId(),c3);
        customerMap.put(c4.getId(),c4);
    }
    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.of(customerMap.get(id));
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDTO) {
        CustomerDTO c = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName(customerDTO.getCustomerName())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        customerMap.put(c.getId(),c);
        return c;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO customerDTO) {
        CustomerDTO c = customerMap.get(id);
        c.setCustomerName(customerDTO.getCustomerName());
        c.setLastModifiedDate(LocalDateTime.now());
        return Optional.of(c);
    }

    @Override
    public Boolean deleteCustomerById(UUID id) {
        customerMap.remove(id);
        return true;
    }
    @Override
    public Boolean patchCustomerById(UUID id, CustomerDTO customerDTO) {
        CustomerDTO existing = customerMap.get(id);
        if(StringUtils.hasText(customerDTO.getCustomerName())) {
            existing.setCustomerName(customerDTO.getCustomerName());
        }
        return true;
    }
}
