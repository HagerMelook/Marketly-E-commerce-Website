package com.market.backend.controllers;

import com.market.backend.dtos.CommentDTO;
import com.market.backend.dtos.RateDTO;
import com.market.backend.services.RatesService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/accountrates")
public class RatesController {
    private final RatesService ratesService;

    @GetMapping("/{id}")
    ResponseEntity<Slice<RateDTO>> getRates(Pageable pageable, @PathVariable Long id) {
        try {
            Slice<RateDTO> slice = ratesService.getRates(pageable, id);
            return new ResponseEntity<>(slice, HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/product/{productId}/{accountId}")
    ResponseEntity<RateDTO> getComment(@PathVariable Long productId, @PathVariable Long accountId) {
        try {
            RateDTO commentDTO = ratesService.getRateByProductId(accountId, productId);
            return new ResponseEntity<>(commentDTO, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(new RateDTO(), HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    private ResponseEntity<Void> createRate(@RequestBody RateDTO rateDTO, UriComponentsBuilder ucb) {
        try {
            Long id = ratesService.createRate(rateDTO);
            URI locationOfNewCashCard = ucb
                    .path("accountrates/{id}")
                    .buildAndExpand(id)
                    .toUri();
            return ResponseEntity.created(locationOfNewCashCard).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{rateId}")
    private ResponseEntity<Void> putRate(@PathVariable Long rateId, @RequestBody RateDTO rateDTO) {
        try {
            ratesService.updateRate(rateId, rateDTO);
            return ResponseEntity.ok().build();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{rateId}")
    private ResponseEntity<Void> deleteRate(@PathVariable Long rateId) {
        try {
            ratesService.deleteRate(rateId);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
