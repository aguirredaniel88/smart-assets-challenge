package ai.smartassets.challenge.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BrandRequest(@NotBlank String name, String description) {
}
