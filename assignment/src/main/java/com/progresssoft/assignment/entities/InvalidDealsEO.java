package com.progresssoft.assignment.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "invalid_deals")
public class InvalidDealsEO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigDecimal id;

    private String dealId;
    private String fromCurrency;
    private String toCurrency;
    private LocalDateTime dealTime;
    private BigDecimal dealAmount;
}