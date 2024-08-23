package ai.smartassets.challenge.mapper;

import ai.smartassets.challenge.dto.response.PagedResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Mapper
public interface PagedResponseMapper {

    PagedResponseMapper INSTANCE = Mappers.getMapper(PagedResponseMapper.class);


    default <E, T> PagedResponse<T> mapPageToPagedResponse(Page<E> page, Function<E, T> converter) {
        List<T> content = page.getContent().stream()
                .map(converter)
                .toList();

        return new PagedResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
