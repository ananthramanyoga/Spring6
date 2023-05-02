package com.example.spring6restmvc.mappers;

import com.example.spring6restmvc.entities.Customer;
import com.example.spring6restmvc.model.CustomerDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-08T12:59:19-0400",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer customerDtoToCustomer(CustomerDTO customerDTO) {
        if ( customerDTO == null ) {
            return null;
        }

        Customer.CustomerBuilder customer = Customer.builder();

        customer.id( customerDTO.getId() );
        customer.customerName( customerDTO.getCustomerName() );
        customer.version( customerDTO.getVersion() );
        customer.createdDate( customerDTO.getCreatedDate() );
        customer.lastModifiedDate( customerDTO.getLastModifiedDate() );

        return customer.build();
    }

    @Override
    public CustomerDTO customerToCustomerDto(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerDTO.CustomerDTOBuilder customerDTO = CustomerDTO.builder();

        customerDTO.id( customer.getId() );
        customerDTO.customerName( customer.getCustomerName() );
        customerDTO.version( customer.getVersion() );
        customerDTO.createdDate( customer.getCreatedDate() );
        customerDTO.lastModifiedDate( customer.getLastModifiedDate() );

        return customerDTO.build();
    }
}
