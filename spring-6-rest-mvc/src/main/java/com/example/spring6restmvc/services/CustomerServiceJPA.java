package com.example.spring6restmvc.services;

import com.example.spring6restmvc.mappers.CustomerMapper;
import com.example.spring6restmvc.model.CustomerDTO;
import com.example.spring6restmvc.repositories.CustomerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {
    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    @Override
    public Boolean patchCustomerById(UUID id, CustomerDTO customerDTO) {
        AtomicBoolean flag = new AtomicBoolean(true);
        customerRepository.findById(id).ifPresentOrElse(foundCustomer -> {
            foundCustomer.setCustomerName(customerDTO.getCustomerName());
            foundCustomer.setLastModifiedDate(LocalDateTime.now());
            customerRepository.save(foundCustomer);
        }, () -> {
            flag.set(false);
        });
        return flag.get();
    }

    @Override
    public Boolean deleteCustomerById(UUID id) {
        if(!customerRepository.findById(id).isEmpty()) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO customerDTO) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
        customerRepository.findById(id).ifPresentOrElse(foundCustomer -> {
            foundCustomer.setCustomerName(customerDTO.getCustomerName());
            foundCustomer.setLastModifiedDate(LocalDateTime.now());
            atomicReference.set(Optional.of(
                    customerMapper.customerToCustomerDto(customerRepository.save(foundCustomer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDTO) {
        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customerDTO)));
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMapper.customerToCustomerDto
                (customerRepository.findById(id).orElse(null)));
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }
}
