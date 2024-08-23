package ai.smartassets.challenge.mapper;

import ai.smartassets.challenge.dto.request.BrandRequest;
import ai.smartassets.challenge.dto.response.BrandResponse;
import ai.smartassets.challenge.model.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BrandMapper {

    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    Brand brandRequestToBrand(BrandRequest brandRequest);

    BrandResponse brandToBrandResponse(Brand brand);
}
