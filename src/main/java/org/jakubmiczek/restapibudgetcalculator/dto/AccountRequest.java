package org.jakubmiczek.restapibudgetcalculator.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountRequest(@NotBlank String name) {}
