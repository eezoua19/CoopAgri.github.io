package com.coopagri.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class HarvestRequest {
    
    @NotBlank(message = "Le produit est requis")
    private String product;
    
    @NotNull(message = "La quantité est requise")
    @Positive(message = "La quantité doit être positive")
    private Double quantity;
    
    @NotBlank(message = "L'unité est requise")
    private String unit;
    
    @NotNull(message = "La date de récolte est requise")
    private LocalDate harvestDate;
    
    @NotBlank(message = "La qualité est requise")
    private String quality;
    
    private Double unitPrice;
    
    @NotNull(message = "L'ID de la coopérative est requis")
    private Long cooperativeId;
    
    @NotNull(message = "L'ID du membre est requis")
    private Long memberId;
    
    private String notes;
}
