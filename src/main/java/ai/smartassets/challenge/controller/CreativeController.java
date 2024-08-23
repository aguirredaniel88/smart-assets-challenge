package ai.smartassets.challenge.controller;

import ai.smartassets.challenge.dto.request.CreativeRequest;
import ai.smartassets.challenge.dto.response.CreativeResponse;
import ai.smartassets.challenge.dto.response.PagedResponse;
import ai.smartassets.challenge.service.CreativeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreativeController {

    private final CreativeService creativeService;

    @PostMapping(path = "/campaigns/{campaignId}/creatives")
    public ResponseEntity<CreativeResponse> saveCreative(@PathVariable String campaignId, @Valid @RequestBody CreativeRequest creativeRequest) {
        CreativeResponse creativeResponse = creativeService.saveCreative(campaignId, creativeRequest);
        return new ResponseEntity<>(creativeResponse, HttpStatus.CREATED);
    }

    @GetMapping(path = "/campaigns/{campaignId}/creatives")
    public PagedResponse<CreativeResponse> findCreativeByCampaignId(@PathVariable String campaignId,
                                                            @RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "50") Integer size) {
        return creativeService.findCreativeByCampaignId(campaignId, page, size);
    }

    @GetMapping(path = "/creatives/{creativeId}")
    public CreativeResponse findCreativeById(@PathVariable String creativeId) {
        return creativeService.findCreativeById(creativeId);
    }
}
