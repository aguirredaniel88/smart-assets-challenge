package ai.smartassets.challenge.dto.response;

import java.util.List;

public record PagedResponse<T>(List<T> content, int number, int size, long totalElements, int totalPages,
                               boolean last) {
}
