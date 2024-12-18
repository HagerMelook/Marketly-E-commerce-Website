package com.market.backend.repositories;

import com.market.backend.models.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    List<Rate> findByAccountId(Long id);
    List<Rate> findByProductId(Long id);
    @Query("SELECT AVG(e.rating) FROM Rate e")
    Double findAverageValue();
}