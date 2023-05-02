package com.example.spring6restmvc.repositories;

import com.example.spring6restmvc.entities.Customer;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveNewCustomerTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            Customer savedCustomer = customerRepository.save(Customer.builder()
                    .customerName("New Customer")
                    .build());
            customerRepository.flush();
        });
    }

    @Test
    void testSaveNewCustomer() {
        Customer savedCustomer = customerRepository.save(Customer.builder()
                .customerName("New Customer")
                        .build());
        customerRepository.flush();
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
    }

}