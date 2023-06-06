package com.progresssoft.assignment.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DealEntityValidator
@Table(name="DEALS")

public class DealsEO {
    @Id
    @Column(name = "ID", nullable = false)
    private String id;

    @Column(name = "FROM_CURRENCY")
    private String fromCurrency;

    @Column(name = "TO_CURRENCY")
    private String toCurrency;

    @NotNull
    @Column(name = "DEAL_TIME")
    private LocalDateTime dealTime;

    @NotNull
    @Column(name = "DEAL_AMOUNT")
    private BigDecimal dealAmount;

  
}
