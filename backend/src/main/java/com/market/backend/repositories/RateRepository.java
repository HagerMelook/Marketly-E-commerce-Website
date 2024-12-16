package com.market.backend.repositories;

import com.market.backend.models.Rate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    Slice<Rate> findAllByProductId(Long id, Pageable pageable);

    Slice<Rate> findAllByAccountId(Long id, Pageable pageable);
    @Query("SELECT AVG(e.rating) FROM Rate e")
    Double findAverageValue();
}