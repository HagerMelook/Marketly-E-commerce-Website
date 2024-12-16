package com.market.backend.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import com.market.backend.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Slice<Comment> findAllByProductId(Long id, Pageable pageable);

    Slice<Comment> findAllByAccountId(Long id, Pageable pageable);
    @Query("SELECT AVG(e.rating) FROM Comment e")
    Double findAverageValue();
}
