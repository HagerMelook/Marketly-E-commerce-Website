package com.market.backend.controllers;

import com.market.backend.dtos.CommentDTO;
import com.market.backend.services.CommentsService;
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
@RequestMapping("/accountcomments")
public class CommentsController {
    private final CommentsService commentsService;

    @GetMapping("/{id}")
    ResponseEntity<Slice<CommentDTO>> getComments(Pageable pageable, @PathVariable Long id) {
        try {
            Slice<CommentDTO> slice = commentsService.getComments(pageable, id);
            return new ResponseEntity<>(slice, HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/product/{productId}/{accountId}")
    ResponseEntity<CommentDTO> getComment(@PathVariable Long productId, @PathVariable Long accountId) {
        try {
            CommentDTO commentDTO = commentsService.getCommentByProductId(accountId, productId);
            return new ResponseEntity<>(commentDTO, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<Void> createComment(@RequestBody CommentDTO commentDTO, UriComponentsBuilder ucb) {
        try {
            Long id = commentsService.createComment(commentDTO);
            URI locationOfNewCashCard = ucb
                    .path("accountcomments/{id}")
                    .buildAndExpand(id)
                    .toUri();
            return ResponseEntity.created(locationOfNewCashCard).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{commentId}")
    private ResponseEntity<Void> putComment(@PathVariable Long commentId, @RequestBody CommentDTO commentDTO) {
        try {
            commentsService.updateComment(commentId, commentDTO);
            return ResponseEntity.ok().build();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{commentId}")
    private ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        try {
            commentsService.deleteComment(commentId);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
