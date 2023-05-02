package com.example.spring6restmvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CustomerDTO {

    private UUID id;
    @NotBlank
    @NotNull
    private String customerName;
    private int version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

}
