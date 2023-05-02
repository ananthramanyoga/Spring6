package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.model.CustomerDTO;
import com.example.spring6restmvc.services.CustomerService;
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
public class CustomerController {
    public static final String CUSTOMER_PATH = "/api/vi/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";
    private final CustomerService customerService;

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity patchCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customerDTO) {
        if(!customerService.patchCustomerById(customerId, customerDTO)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteCustomerById(@PathVariable("customerId") UUID customerId) {
        if(!customerService.deleteCustomerById(customerId)) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID id, @RequestBody CustomerDTO customerDTO) {
        if(customerService.updateCustomerById(id, customerDTO).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity saveNewCustomer(@Validated @RequestBody CustomerDTO customerDTO) {
        CustomerDTO c = customerService.saveNewCustomer(customerDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CUSTOMER_PATH + "/" + c.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> listCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID id) {
        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }
}
