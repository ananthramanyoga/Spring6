package com.example.spring6restmvc.mappers;

import com.example.spring6restmvc.entities.Customer;
import com.example.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDto(Customer customer);
}
