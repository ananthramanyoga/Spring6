package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.services.CustomerService;
import com.example.spring6restmvc.services.CustomerServiceImpl;
import com.example.spring6restmvc.model.CustomerDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerDTOControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;
    @Autowired
    ObjectMapper objectMapper;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

    @Test
    void testPatchCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "New Name");
        given(customerService.patchCustomerById(any(),any())).willReturn(true);
        mockMvc.perform(patch( CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(),
                customerArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customer.getId());
        assertThat(customerArgumentCaptor.getValue().getCustomerName())
                .isEqualTo(customerMap.get("customerName"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);

        given(customerService.deleteCustomerById(any())).willReturn(true);
        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);

        given(customerService.updateCustomerById(any(),any())).willReturn(Optional.of(customer));

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .content(objectMapper.writeValueAsString(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerById(uuidArgumentCaptor.capture(), any(CustomerDTO.class));

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testCreateCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);
        customer.setId(null);
        customer.setVersion(0);

        given(customerService.saveNewCustomer(any(CustomerDTO.class)))
                .willReturn(customerServiceImpl.listCustomers().get(1));

        mockMvc.perform(post(CustomerController.CUSTOMER_PATH).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
    @Test
    void testCreateCustomerNullName() throws Exception {
        CustomerDTO customerDTO = CustomerDTO.builder().build();

        given(customerService.saveNewCustomer(any(CustomerDTO.class)))
                .willReturn(customerServiceImpl.listCustomers().get(1));

        MvcResult mvcResult = mockMvc.perform(post(CustomerController.CUSTOMER_PATH).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    void listAllCustomers() throws Exception {
        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(4)));
    }

    @Test
    void getCustomerByIdNotFound() throws Exception {

        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomerById() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);

        given(customerService.getCustomerById(customer.getId())).willReturn(Optional.of(customer));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerName", is(customer.getCustomerName())));
    }
}