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

    @Transactional
    public CommentDTO getCommentByProductId(Long accountId, Long productId) {
        Comment comment = commentRepository.findByAccountIdAndProductId(accountId, productId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));

        return new CommentDTO(comment);
    }

    private void updateRate(Product product) {
        Long allCommentsRate = commentRepository.findSumValueByProductId(product.getId());
        Long commentsCount = commentRepository.countByProductId(product.getId());
        Long allRatesRate = rateRepository.findSumValueByProductId(product.getId());
        Long ratesCount = rateRepository.countByProductId(product.getId());
        allCommentsRate = allCommentsRate==null ? 0 : allCommentsRate;
        allRatesRate = allRatesRate==null ? 0 : allRatesRate;
        if (ratesCount+commentsCount==0) {
            product.setRating(0);
        }
        else {
            product.setRating((double) (allRatesRate + allCommentsRate) /(ratesCount+commentsCount));
        }
    }

    private boolean reviewExists(Long accountId, Long productId) {
        return commentRepository.findByAccountIdAndProductId(accountId,productId).isPresent()
                || rateRepository.findByAccountIdAndProductId(accountId, productId).isPresent();
    }

    @Transactional
    public Long createComment(CommentDTO commentDTO) {
        Product product = productRepository.findById(commentDTO.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        Account account = accountRepository.findById(commentDTO.getAccountId())
                .orElseThrow(() -> new NoSuchElementException("Account not found"));

        System.out.println("prod:"+product.getId());
        System.out.println("acc:"+(account.getId()));
        if (reviewExists(account.getId(), product.getId())) {
            throw new IllegalArgumentException("Review already exists");
        }

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
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));
        Product product = comment.getProduct();

        commentRepository.deleteById(id);

        updateRate(product);
    }
}
