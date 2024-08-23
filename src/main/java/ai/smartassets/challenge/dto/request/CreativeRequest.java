package ai.smartassets.challenge.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreativeRequest(@NotBlank String name, String description, @NotBlank String creativeUrl) {
}
