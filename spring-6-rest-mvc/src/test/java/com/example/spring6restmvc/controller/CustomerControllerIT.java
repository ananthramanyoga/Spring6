package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.entities.Customer;
import com.example.spring6restmvc.mappers.CustomerMapper;
import com.example.spring6restmvc.model.CustomerDTO;
import com.example.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class CustomerControllerIT {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void testPatchCustomerNotFound() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        String name = "PatchedCustomer";
        customerDTO.setCustomerName(name);
        ResponseEntity responseEntity = customerController.patchCustomerById(UUID.randomUUID(),customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
    }

    @Test
    @Rollback
    @Transactional
    void testPatchCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        String name = "PatchedCustomer";
        customerDTO.setCustomerName(name);

        ResponseEntity responseEntity = customerController.patchCustomerById(customer.getId(),customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(customerRepository.findById(customer.getId()).get().getCustomerName()).isEqualTo(name);
    }
    @Test
    void testDeleteCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteCustomerById(UUID.randomUUID());
        });
    }

    @Test
    @Rollback
    @Transactional
    void testDeleteCustomer() {
        Customer customer = customerRepository.findAll().get(0);

        ResponseEntity responseEntity = customerController.deleteCustomerById(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void testUpdateCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Test
    @Rollback
    @Transactional
    void testUpdateExistingCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        String name = "UpdatedCustomer";
        customerDTO.setCustomerName(name);
        customerDTO.setId(null);
        customerDTO.setVersion(customer.getVersion());

        ResponseEntity responseEntity = customerController.updateCustomerById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer customer1 = customerRepository.findById(customer.getId()).get();
        assertThat(customer1.getCustomerName()).isEqualTo(name);
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("Test Customer")
                .build();

        ResponseEntity responseEntity = customerController.saveNewCustomer(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");

        UUID uuid = UUID.fromString(locationUUID[4]);
        Customer customer = customerRepository.findById(uuid).get();

        assertThat(customer).isNotNull();
    }

    @Test
    void testGetCustomerByIdNotFound() {
        assertThrows(NotFoundException.class,
                () -> customerController.getCustomerById(UUID.randomUUID()));

    }

    @Test
    void testGetCustomerById() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());

        assertThat(customerDTO).isNotNull();
    }

    @Test
    void testListCustomers() {
        List<CustomerDTO> customerDTOList = customerController.listCustomers();
        assertThat(customerDTOList.size()).isEqualTo(4);
    }
    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        customerRepository.deleteAll();
        List<CustomerDTO> customerDTOList = customerController.listCustomers();
        assertThat(customerDTOList.size()).isEqualTo(0);

    }
}
