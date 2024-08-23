package ai.smartassets.challenge.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Campaign {

    @Id
    private String campaignId;
    private String name;
    private String description;
    private String brandId;

}
