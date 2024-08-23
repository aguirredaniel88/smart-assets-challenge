package ai.smartassets.challenge.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class InitializationState {
    private boolean initialized = false;

}
