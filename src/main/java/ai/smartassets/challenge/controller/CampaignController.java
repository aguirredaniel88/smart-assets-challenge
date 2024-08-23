package ai.smartassets.challenge.controller;

import ai.smartassets.challenge.dto.request.CampaignRequest;
import ai.smartassets.challenge.dto.response.CampaignResponse;
import ai.smartassets.challenge.dto.response.PagedResponse;
import ai.smartassets.challenge.service.CampaignService;
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
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping(path = "/brands/{brandId}/campaigns")
    public ResponseEntity<CampaignResponse> saveCampaign(@PathVariable String brandId, @Valid @RequestBody CampaignRequest campaignRequest){
        CampaignResponse campaignResponse = campaignService.saveCampaign(brandId, campaignRequest);
        return new ResponseEntity<>(campaignResponse, HttpStatus.CREATED);
    }

    @GetMapping(path = "/brands/{brandId}/campaigns")
    public PagedResponse<CampaignResponse> findCampaignsByBrandId(@PathVariable String brandId,
                                                               @RequestParam(defaultValue = "0") Integer page,
                                                               @RequestParam(defaultValue = "50") Integer size ) {
        return campaignService.findCampaignsByBrandId(brandId, page, size);
    }

    @GetMapping(path = "/campaigns/{campaignId}")
    public CampaignResponse findCampaignById(@PathVariable String campaignId) {
        return campaignService.findCampaignById(campaignId);
    }
}
