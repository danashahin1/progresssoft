package com.progresssoft.assignment.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor


public class DealsDTO {
    
    private String id;
    private String fromCurrency;
    private String toCurrency;
    private LocalDateTime dealTime;
    private BigDecimal dealAmount;


}
