package com.progresssoft.assignment.entities;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.progresssoft.assignment.AppContextProvider;
import com.progresssoft.assignment.repositories.DealsRepository;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DealEntityValidator.EntityValidatorImpl.class)
@Documented
public @interface DealEntityValidator {
    String message() default "Invalid Record";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Slf4j
    @NoArgsConstructor
    public class EntityValidatorImpl implements ConstraintValidator<DealEntityValidator, DealsEO> {
       
        // to validate the duplication of deal's IDs
        private static BiPredicate<DealsEO, ConstraintValidatorContext> validateUniqueDeal = (deal,
                context) -> {
            // was created to test id duplication but SQLException was cought instead.
           
            // DealsRepository     dealsRepository    =AppContextProvider.getContext().getBean(DealsRepository.class);
            // Optional<Deals> prevDeal= dealsRepository.findById(deal.getId());
            // if(prevDeal.isPresent() )
            
            // {
            //     context.disableDefaultConstraintViolation();
            //     context.buildConstraintViolationWithTemplate("{Duplicated Record}").addConstraintViolation();
            //     return false;
            // }
                 return true;

        };

        // to validate amount value

        private static BiPredicate<DealsEO, ConstraintValidatorContext> validateDealAmount = (
                deal, context) -> {
                    if(deal.getDealAmount()!=null)
           { if(deal.getDealAmount().compareTo(BigDecimal.ZERO)<=0)
            {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{Amount Must Be >0}").addConstraintViolation();
                return false;
            }
        }
            return true;
        };


// To validate deal date format
        private static BiPredicate<DealsEO, ConstraintValidatorContext> validateDate = (
            deal, context) -> {
                try {
                    if(deal.getDealTime()!=null)
                   { System.out.print(deal.getDealTime().toString());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.US);
                    LocalDateTime.parse(deal.getDealTime().toString(), formatter);
                   }
                }
                 catch (DateTimeParseException e) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Invalid Date Format").addConstraintViolation();
                    return false;
                }
                return true;
                
       
    };
        

        @Override
        public boolean isValid(DealsEO iosBasicInfo, ConstraintValidatorContext context) {
            return Optional.of(iosBasicInfo)
                    .filter(itm -> validateUniqueDeal.and(validateDealAmount).and(validateDate).test(itm, context))
                    .map(x -> Boolean.TRUE).orElse(Boolean.FALSE);
        }



      

    }
}