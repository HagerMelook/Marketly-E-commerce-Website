package com.market.backend.services;

import com.market.backend.dtos.RateDTO;
import com.market.backend.models.Account;
import com.market.backend.models.Product;
import com.market.backend.models.Rate;
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
public class RatesService {
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;
    private final RateRepository rateRepository;

    @Transactional
    public Slice<RateDTO> getRates(Pageable pageable, Long id) {
        Slice<Rate> slice = rateRepository.findAllByAccountId(id ,
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))
                ));
        return slice.map(RateDTO::new);
    }

    private void updateProductRate(Product product) {
        Double allCommentsRate = commentRepository.findAverageValue();
        Long commentsCount = commentRepository.count();
        Double allRatesRate = rateRepository.findAverageValue();
        Long ratesCount = rateRepository.count();
        product.setRating((allRatesRate*ratesCount+allCommentsRate*commentsCount)/(ratesCount+commentsCount));
    }

    @Transactional
    public Long createRate(RateDTO rateDTO) {
        Product product = productRepository.findById(rateDTO.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        Account account = accountRepository.findById(rateDTO.getAccountId())
                .orElseThrow(() -> new NoSuchElementException("Account not found"));

        Rate rate = new Rate(null, product, account, rateDTO.getRating());
        Rate newRate = rateRepository.save(rate);

        updateProductRate(product);

        return newRate.getId();
    }

    @Transactional
    public void updateRate(Long id, RateDTO rateDTO) {
        Rate rate = rateRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Rate not found"));

        if (!rate.getAccount().getId().equals(rateDTO.getAccountId()) ||
                !rate.getProduct().getId().equals(rateDTO.getProductId())) {
            throw new IllegalArgumentException();
        }

        rate.setRating(rateDTO.getRating());
        updateProductRate(rate.getProduct());
    }

    @Transactional
    public void deleteRate(Long id) {
        rateRepository.deleteById(id);
    }
}
