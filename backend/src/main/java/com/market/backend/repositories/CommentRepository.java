package com.market.backend.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import com.market.backend.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Slice<Comment> findAllByProductId(Long id, Pageable pageable);

    Slice<Comment> findAllByAccountId(Long id, Pageable pageable);
    @Query("SELECT SUM(e.rating) FROM Comment e WHERE e.product.id = :id")
    Long findSumValueByProductId(@Param("id")Long id);

    Long countByProductId(Long id);

    Optional<Comment> findByAccountIdAndProductId(Long accountId, Long productId);
}
