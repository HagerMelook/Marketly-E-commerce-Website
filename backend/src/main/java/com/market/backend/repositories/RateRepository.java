package com.market.backend.repositories;

import com.market.backend.models.Rate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    Slice<Rate> findAllByProductId(Long id, Pageable pageable);

    Slice<Rate> findAllByAccountId(Long id, Pageable pageable);
    @Query("SELECT SUM(e.rating) FROM Rate e WHERE e.product.id = :id")
    Long findSumValueByProductId(@Param("id")Long id);

    Long countByProductId(Long id);

    Optional<Rate> findByAccountIdAndProductId(Long accountId, Long productId);
}