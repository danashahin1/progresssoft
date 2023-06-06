package com.progresssoft.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.progresssoft.assignment.DTO.DealsDTO;
import com.progresssoft.assignment.controllers.DealsController;
import com.progresssoft.assignment.entities.InvalidDealsEO;
import com.progresssoft.assignment.exceptions.DealValidityException;
import com.progresssoft.assignment.repositories.DealsRepository;
import com.progresssoft.assignment.services.DealSearvice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DealsTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private DealsRepository dealsRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private DealSearvice dealSearvice;

    @InjectMocks
    private DealsController dealsController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
// test if valid data
    @Test
    public void testInsertValidDeal() throws DealValidityException {
        DealsDTO deal = new DealsDTO();
        deal.setId("NEW Deal" + new Random());
        deal.setFromCurrency("USD");
        deal.setToCurrency("EUR");
        deal.setDealAmount(BigDecimal.valueOf(500));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.US);
        deal.setDealTime(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));
        assertEquals(getValidDealValidationResult(), dealSearvice.dealValidator(deal));
        Object response = dealSearvice.insertDeal(deal);
        // assertEquals("Deal Inserted Successfully", response);
        verify(entityManager).persist(any());

    }

// test  invalid data check
@Test
public void testCheckInvalidDeal() {
    
        DealsDTO dealDto = new DealsDTO();

        assertFalse((boolean)dealSearvice.dealValidator(dealDto).get("result"));
    }

// test if invalid data(insert)
    @Test
    public void testInsertInvalidDeal() {
        { 
            DealsDTO dealDto = new DealsDTO();

            assertFalse((boolean)dealSearvice.dealValidator(dealDto).get("result"));
        }
        
        DealsDTO deal = new DealsDTO();
        deal.setId("NEW Deal" + new Random());
        deal.setFromCurrency("USD");
        deal.setToCurrency("XYZ"); // Invalid currency
        deal.setDealAmount(BigDecimal.valueOf(-1000)); // Invalid amount
        deal.setDealTime(LocalDateTime.now());
        assertEquals(getInvalidDealValidationResult().get("result"), dealSearvice.dealValidator(deal).get("result"));
        assertThrows(DealValidityException.class, () -> {
            dealSearvice.insertDeal(deal);
        });

        verify(entityManager).persist(any(InvalidDealsEO.class));
    }

    private Map<String, Object> getValidDealValidationResult() {
        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        return result;
    }

    private Map<String, Object> getInvalidDealValidationResult() {
        Map<String, Object> result = new HashMap<>();
        result.put("result", false);
        return result;
    }

    // controller test
    @Test
    public void createDealController() throws Exception {
        DealsDTO deal = new DealsDTO();
        ResultHandler handler = new ResultHandler() {
            public void handle(MvcResult result) {

                deal.setId("NEW Deal" + new Random());
                deal.setFromCurrency("USD");
                deal.setToCurrency("EUR");
                deal.setDealAmount(BigDecimal.valueOf(500));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.US);
                deal.setDealTime(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));

            }

        };
        mockMvc.perform(
                post("/rest/v1/save-deal").contentType(MediaType.APPLICATION_JSON).content(convertToJsonStr(deal)))
                .andExpect(status().isOk()).andDo(handler).andReturn();

    }

    private static String convertToJsonStr(Object o) throws JsonProcessingException {
        ObjectMapper oM = new ObjectMapper();
        oM.registerModule(new JavaTimeModule());
        oM.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return oM.writeValueAsString(o);
    }
}
