package com.akkor.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO pour l'authentification d'un utilisateur")
public class AuthenticationRequest {
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Schema(example = "john.doe@example.com")
    private String email;
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Schema(example = "Test123@")
    private String password;
} 