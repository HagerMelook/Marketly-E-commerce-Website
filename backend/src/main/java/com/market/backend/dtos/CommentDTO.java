package com.market.backend.dtos;

import com.market.backend.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private Long accountId;
    private Long productId;
    private String name;
    private String body;
    private Integer rating;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.body = comment.getBody();
        this.rating  = comment.getRating();
        this.accountId = comment.getAccount().getId();
        this.productId = comment.getProduct().getId();
        this.name = comment.getAccount().getUsername();
    }
}
