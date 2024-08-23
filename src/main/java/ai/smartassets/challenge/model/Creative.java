package ai.smartassets.challenge.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Creative {

    @Id
    private String creativeId;
    private String name;
    private String description;
    private String creativeUrl;
    private String campaignId;

}
