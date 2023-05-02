package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    Boolean patchCustomerById(UUID id, CustomerDTO customerDTO);
    Boolean deleteCustomerById(UUID id);
    Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO customerDTO);
    CustomerDTO saveNewCustomer(CustomerDTO customerDTO);
    Optional<CustomerDTO> getCustomerById(UUID id);
    List<CustomerDTO> listCustomers();
}
