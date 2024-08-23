package ai.smartassets.challenge.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HealthCheckIndicator implements HealthIndicator {

    private static final String MONGO_DB_TEST_DATA_INSERTION = "MongoDB test data insertion";
    private static final String COMPLETED = "Completed";
    private static final String IN_PROGRESS = "In Progress";
    private final InitializationState initializationState;


    @Override
    public Health health() {
        if (initializationState.isInitialized()) {
            return Health.up().withDetail(MONGO_DB_TEST_DATA_INSERTION, COMPLETED).build();
        }
        return Health.down().withDetail(MONGO_DB_TEST_DATA_INSERTION, IN_PROGRESS).build();
    }
}
