package com.moodjournal.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateActivityRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String icon;   // nom d'icône Material (ex: "fitness_center")

    @NotBlank
    private String color;  // code hexadécimal ex: "#FF5722"
}