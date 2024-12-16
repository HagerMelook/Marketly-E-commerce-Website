package com.market.backend.services;

import com.market.backend.dtos.CommentDTO;
import com.market.backend.models.Account;
import com.market.backend.models.Comment;
import com.market.backend.models.Product;
import com.market.backend.repositories.AccountRepository;
import com.market.backend.repositories.CommentRepository;
import com.market.backend.repositories.ProductRepository;
import com.market.backend.repositories.RateRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class CommentsService {
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;
    private final RateRepository rateRepository;

    @Transactional
    public Slice<CommentDTO> getComments(Pageable pageable, Long id) {
        Slice<Comment> slice = commentRepository.findAllByAccountId(id ,
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))
                ));
        return slice.map(CommentDTO::new);
    }

    private void updateRate(Product product) {
        Double allCommentsRate = commentRepository.findAverageValue();
        Long commentsCount = commentRepository.count();
        Double allRatesRate = rateRepository.findAverageValue();
        Long ratesCount = rateRepository.count();
        product.setRating((allRatesRate*ratesCount+allCommentsRate*commentsCount)/(ratesCount+commentsCount));
    }

    @Transactional
    public Long createComment(CommentDTO commentDTO) {
        Product product = productRepository.findById(commentDTO.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        Account account = accountRepository.findById(commentDTO.getAccountId())
                .orElseThrow(() -> new NoSuchElementException("Account not found"));

        Comment comment = new Comment(null, product, account, commentDTO.getBody(), commentDTO.getRating());
        Comment newComment = commentRepository.save(comment);

        updateRate(product);

        return newComment.getId();
    }

    @Transactional
    public void updateComment(Long id, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));

        if (!comment.getAccount().getId().equals(commentDTO.getAccountId()) ||
            !comment.getProduct().getId().equals(commentDTO.getProductId())) {
            throw new IllegalArgumentException();
        }

        comment.setBody(commentDTO.getBody());
        comment.setRating(commentDTO.getRating());
        updateRate(comment.getProduct());
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
