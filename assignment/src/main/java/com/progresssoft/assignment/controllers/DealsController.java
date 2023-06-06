package com.progresssoft.assignment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.progresssoft.assignment.DTO.DealsDTO;
import com.progresssoft.assignment.entities.DealsEO;
import com.progresssoft.assignment.entities.InvalidDealsEO;
import com.progresssoft.assignment.exceptions.DealValidityException;
import com.progresssoft.assignment.repositories.DealsRepository;
import com.progresssoft.assignment.services.DealSearvice;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class DealsController {
@Autowired
DealsRepository dealsRepository;

@Autowired 
DealSearvice dealSearvice;


    @GetMapping(path = "/rest/v1/get-all-deals")
    public @ResponseBody Object getDeals() {
        return dealsRepository.findAll();
    }
    
    @Transactional
    @PostMapping(path = "/rest/v1/save-deal")
    public @ResponseBody Object saveDeal(@RequestBody DealsDTO deal) throws DealValidityException {
        return dealSearvice.insertDeal(deal);
    }
}
