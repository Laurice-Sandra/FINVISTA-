package com.PI.Finvista.dto.responses;


import jakarta.persistence.Entity;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class MessageResponse {
    private String message;
}
