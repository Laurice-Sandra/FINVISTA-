package com.PI.Finvista.dto.requests;


import com.PI.Finvista.entities.enumerations.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {


    private String firstname ;
    private String lastname ;
    private String email ;
    private String password ;
    private Role role ;
}

