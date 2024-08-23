package ai.smartassets.challenge.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> clazz, String entityId) {
        super(String.format("%s with id %s not found", clazz.getSimpleName(), entityId));
    }
}
