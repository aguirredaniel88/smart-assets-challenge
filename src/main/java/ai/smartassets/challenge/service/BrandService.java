package ai.smartassets.challenge.service;

import ai.smartassets.challenge.dto.request.BrandRequest;
import ai.smartassets.challenge.dto.response.BrandResponse;
import ai.smartassets.challenge.dto.response.PagedResponse;
import ai.smartassets.challenge.exception.EntityNotFoundException;
import ai.smartassets.challenge.mapper.BrandMapper;
import ai.smartassets.challenge.mapper.PagedResponseMapper;
import ai.smartassets.challenge.model.Brand;
import ai.smartassets.challenge.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandResponse save(BrandRequest brandRequest) {
        Brand brand = BrandMapper.INSTANCE.brandRequestToBrand(brandRequest);
        brand.setBrandId(UUID.randomUUID().toString());

        return BrandMapper.INSTANCE.brandToBrandResponse(brandRepository.save(brand));
    }

    public PagedResponse<BrandResponse> findAll(Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<Brand> brands = brandRepository.findAll(pageRequest);
        return PagedResponseMapper.INSTANCE.mapPageToPagedResponse(brands, BrandMapper.INSTANCE::brandToBrandResponse);
    }

    public BrandResponse findById(String brandId)  {
        return brandRepository.findById(brandId)
                .map(BrandMapper.INSTANCE::brandToBrandResponse)
                .orElseThrow(() -> new EntityNotFoundException(Brand.class, brandId));
    }

    public void validateBrandExists(String brandId) {
        if (brandDoesNotExist(brandId)) {
            throw new EntityNotFoundException(Brand.class, brandId);
        }
    }

    private boolean brandDoesNotExist(String brandId) {
        return !brandRepository.existsById(brandId);
    }
}
