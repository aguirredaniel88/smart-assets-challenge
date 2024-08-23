package ai.smartassets.challenge.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CampaignRequest(@NotBlank String name, String description) {
}
