package ai.smartassets.challenge.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Brand {

    @Id
    private String brandId;
    private String name;
    private String description;
}
