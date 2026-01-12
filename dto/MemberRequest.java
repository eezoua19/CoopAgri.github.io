package com.coopagri.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MemberRequest {
    
    @NotBlank(message = "Le prénom est requis")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String firstName;
    
    @NotBlank(message = "Le nom est requis")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String lastName;
    
    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email n'est pas valide")
    private String email;
    
    @NotBlank(message = "Le téléphone est requis")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Le format du téléphone n'est pas valide")
    private String phone;
    
    @NotNull(message = "La date d'adhésion est requise")
    private LocalDate joinDate;
    
    @NotNull(message = "L'ID de la coopérative est requis")
    private Long cooperativeId;
    
    private Boolean isActive = true;
}
