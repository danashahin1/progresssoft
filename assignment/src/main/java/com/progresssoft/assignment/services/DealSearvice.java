package com.progresssoft.assignment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.progresssoft.assignment.DTO.DealsDTO;
import com.progresssoft.assignment.entities.DealsEO;
import com.progresssoft.assignment.entities.InvalidDealsEO;
import com.progresssoft.assignment.exceptions.DealValidityException;
import com.progresssoft.assignment.repositories.DealsRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class DealSearvice {
    @Autowired
    DealsRepository dealsRepository;

    @PersistenceContext
    EntityManager em;
    private static final Logger logger = LoggerFactory.getLogger(DealSearvice.class);

    public @ResponseBody Object insertDeal(DealsDTO deal) throws DealValidityException {
        logger.info("------------------check deal validity-----------------");
        Map result = dealValidator(deal);
        boolean valid = (boolean) result.get("result");
        if (valid)// insert into deals table
        {
            logger.info("---------------deal is valid , will be inserted into deals table-------------");
            DealsEO d = new DealsEO();
            d.setId(deal.getId());
            d.setFromCurrency(deal.getFromCurrency());
            d.setToCurrency(deal.getToCurrency());
            d.setDealAmount(deal.getDealAmount());
            d.setDealTime(deal.getDealTime());
            em.persist(d);
        }

        else // insert into invalid deals table
        {
            logger.warn("-----------------deal is not valid-------------------");
            InvalidDealsEO invDeal = new InvalidDealsEO();
            invDeal.setDealId(deal.getId());
            invDeal.setFromCurrency(deal.getFromCurrency());
            invDeal.setToCurrency(deal.getToCurrency());
            invDeal.setDealAmount(deal.getDealAmount());
            invDeal.setDealTime(deal.getDealTime());
            em.persist(invDeal);
            throw new DealValidityException("Deal is not Valid ,Please Check " + (String) result.get("field")
                    + ", record Inserted into invalid deals table");
        }

        return new ResponseEntity<>("Deal Inserted Successfully", HttpStatus.OK);
    }

    public Map dealValidator(DealsDTO dealDto) {

        // check available iso currency code
        Set<String> codes = getCurrencyCodes();

        // to put the result including the field that has errors
        Map result = new HashMap<String, Object>();
        String field = null;

        logger.info("---------------Check Deal ID if exist / previously added-------------");

        if (dealDto.getId() == null) {

            field = "ID";

        } else {

            Optional<DealsEO> prevDeal = dealsRepository.findById(dealDto.getId());
            if (prevDeal.isPresent())

            {
                field = "ID";

            }

        }
        logger.info("---------------Check The existance of currency and currency code format-------------");

        if (dealDto.getToCurrency() == null) {

            {
                field = "To Currency";

            }
        } else {
            if (!codes.contains(dealDto.getToCurrency())) {
                field = "To Currency";
            }
        }

        if (dealDto.getFromCurrency() == null) {

            {
                field = "From Currency";

            }
        } else {
            if (!codes.contains(dealDto.getFromCurrency())) {
                field = "From Currency";
            }
        }

        logger.info("---------------Check Date Time Format-------------");

        if (dealDto.getDealTime() == null) {

            {
                field = "Deal Time";

            }
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.US);
                LocalDateTime.parse(dealDto.getDealTime().toString(), formatter);
            } catch (DateTimeParseException e) {
                field = "Deal Time";

            }
        }
        logger.info("---------------Check Deal Amount-------------");

        if (dealDto.getDealAmount() == null) {
            {
                field = "Deal Amount";

            }
        } else {
            if (dealDto.getDealAmount().compareTo(BigDecimal.ZERO) <= 0)

            {
                field = "Deal Amount";

            }
        }

        if (field != null) {
            result.put("result", false);
            result.put("field", field);
            return result;
        }

        result.put("result", true);
        return result;
    }

    public Set<String> getCurrencyCodes()

    {
        Set<Currency> crn = Currency.getAvailableCurrencies();
        Set<String> codes = new HashSet<String>();
        crn.forEach(c -> {
            codes.add(c.getCurrencyCode());

        });
        return codes;

    }
}
