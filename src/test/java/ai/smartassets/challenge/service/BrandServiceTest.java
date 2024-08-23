package ai.smartassets.challenge.service;

import ai.smartassets.challenge.dto.request.BrandRequest;
import ai.smartassets.challenge.dto.response.BrandResponse;
import ai.smartassets.challenge.dto.response.PagedResponse;
import ai.smartassets.challenge.exception.EntityNotFoundException;
import ai.smartassets.challenge.model.Brand;
import ai.smartassets.challenge.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    @Captor
    ArgumentCaptor<Brand> brandArgumentCaptor;


    @Test
    void saveBrand_brandSavedSuccessfully() {
        String brandName = "my brand name";
        String brandDescription = "my brand description";
        Brand brand = createBrand(brandName, brandDescription);
        BrandRequest brandRequest = new BrandRequest(brandName, brandDescription);
        BrandResponse expectedResponse = new BrandResponse(brand.getBrandId(), brand.getName(), brand.getDescription());


        when(brandRepository.save(any(Brand.class))).thenReturn(brand);


        BrandResponse actualResponse = brandService.save(brandRequest);

        verify(brandRepository).save(brandArgumentCaptor.capture());

        Brand actualBrand = brandArgumentCaptor.getValue();

        assertThat(actualBrand.getBrandId()).isNotBlank();
        assertThat(actualBrand.getName()).isEqualTo(brandName);
        assertThat(actualBrand.getDescription()).isEqualTo(brandDescription);

        assertThat(actualResponse).isEqualTo(expectedResponse);

    }

    @Test
    void findAllBrands_brandsFoundSuccessfully() {
        int page = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<Brand> brandPage = mock(Page.class);
        String brandName = "my brand name";
        String brandDescription = "my brand description";
        Brand brand = createBrand(brandName, brandDescription);

        PagedResponse<BrandResponse> expectedResponse = new PagedResponse<>(
                List.of(new BrandResponse(brand.getBrandId(), brandName, brandDescription)),
                page,
                pageSize,
                1L,
                1,
                true);

        when(brandPage.getTotalElements()).thenReturn(1L);
        when(brandPage.getTotalPages()).thenReturn(1);
        when(brandPage.getContent()).thenReturn(List.of(brand));
        when(brandPage.isLast()).thenReturn(true);
        when(brandPage.getSize()).thenReturn(pageSize);
        when(brandPage.getNumber()).thenReturn(page);


        when(brandRepository.findAll(pageRequest)).thenReturn(brandPage);

        PagedResponse<BrandResponse> actualResponse = brandService.findAll(page, pageSize);

        verify(brandRepository).findAll(pageRequest);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void findBrandById_brandFoundSuccessfully() {
        String brandName = "my brand name";
        String brandDescription = "my brand description";
        Brand brand = createBrand(brandName, brandDescription);
        BrandResponse expectedResponse = new BrandResponse(brand.getBrandId(), brandName, brandDescription);

        when(brandRepository.findById(brand.getBrandId())).thenReturn(Optional.of(brand));

        BrandResponse actualResponse = brandService.findById(brand.getBrandId());

        verify(brandRepository).findById(brand.getBrandId());

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void findBrandById_brandDoesNotExist_throwsException() {
        String brandId = "invalidBrandId";

        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> brandService.findById(brandId));
    }

    @Test
    void validateBrandExists_brandExists_noExceptionThrown() {
        String brandId = "brandId";

        when(brandRepository.existsById(brandId)).thenReturn(true);

        assertDoesNotThrow(() -> brandService.validateBrandExists(brandId));
    }

    @Test
    void validateBrandExists_brandDoesNotExist_throwsException() {
        String brandId = "invalidBrandId";

        when(brandRepository.existsById(brandId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> brandService.validateBrandExists(brandId));
    }

    private static Brand createBrand(String brandName, String brandDescription) {
        String brandId = UUID.randomUUID().toString();

        return Brand.builder()
                .brandId(brandId)
                .name(brandName)
                .description(brandDescription)
                .build();
    }
}
