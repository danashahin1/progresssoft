package com.progresssoft.assignment.repositories;


import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.progresssoft.assignment.entities.DealsEO;

@Repository
public interface DealsRepository extends JpaRepository<DealsEO, String> {
   



}