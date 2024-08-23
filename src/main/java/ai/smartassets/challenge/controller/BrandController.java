package ai.smartassets.challenge.controller;

import ai.smartassets.challenge.dto.request.BrandRequest;
import ai.smartassets.challenge.dto.response.BrandResponse;
import ai.smartassets.challenge.dto.response.PagedResponse;
import ai.smartassets.challenge.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@Validated
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<BrandResponse> saveBrand(@Valid @RequestBody BrandRequest brandRequest) {
        BrandResponse brandResponse = brandService.save(brandRequest);
        return new ResponseEntity<>(brandResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public PagedResponse<BrandResponse> findAllBrands(@RequestParam(defaultValue = "0") Integer page,
                                                      @RequestParam(defaultValue = "50") Integer size) {
        return brandService.findAll(page, size);
    }

    @GetMapping(path = "/{brandId}")
    public BrandResponse findBrandById(@PathVariable String brandId) {
        return brandService.findById(brandId);
    }

}
