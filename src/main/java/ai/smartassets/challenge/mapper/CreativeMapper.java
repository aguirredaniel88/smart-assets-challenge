package ai.smartassets.challenge.mapper;

import ai.smartassets.challenge.dto.request.CreativeRequest;
import ai.smartassets.challenge.dto.response.CreativeResponse;
import ai.smartassets.challenge.model.Creative;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreativeMapper {

    CreativeMapper INSTANCE = Mappers.getMapper(CreativeMapper.class);

    Creative creativeRequestToCreative(CreativeRequest creativeRequest);

    CreativeResponse creativeToCreativeResponse(Creative creative);
}
