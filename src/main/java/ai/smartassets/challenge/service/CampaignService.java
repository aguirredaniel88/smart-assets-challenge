package ai.smartassets.challenge.service;

import ai.smartassets.challenge.dto.request.CampaignRequest;
import ai.smartassets.challenge.dto.response.CampaignResponse;
import ai.smartassets.challenge.dto.response.PagedResponse;
import ai.smartassets.challenge.exception.EntityNotFoundException;
import ai.smartassets.challenge.mapper.CampaignMapper;
import ai.smartassets.challenge.mapper.PagedResponseMapper;
import ai.smartassets.challenge.model.Campaign;
import ai.smartassets.challenge.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final BrandService brandService;

    public CampaignResponse saveCampaign(String brandId, CampaignRequest campaignRequest) {
        brandService.validateBrandExists(brandId);
        Campaign campaign = CampaignMapper.INSTANCE.campaignRequestToCampaign(campaignRequest);
        campaign.setBrandId(brandId);
        campaign.setCampaignId(UUID.randomUUID().toString());

        return CampaignMapper.INSTANCE.campaignToCampaignResponse(campaignRepository.save(campaign));
    }


    public CampaignResponse findCampaignById(String campaignId) {
        return campaignRepository.findById(campaignId)
                .map(CampaignMapper.INSTANCE::campaignToCampaignResponse)
                .orElseThrow(() -> new EntityNotFoundException(Campaign.class, campaignId));
    }

    public PagedResponse<CampaignResponse> findCampaignsByBrandId(String brandId, Integer page, Integer size) {
        brandService.validateBrandExists(brandId);
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Campaign> campaigns = campaignRepository.findByBrandId(brandId, pageRequest);
        return PagedResponseMapper.INSTANCE.mapPageToPagedResponse(campaigns, CampaignMapper.INSTANCE::campaignToCampaignResponse);
    }

    public void validateCampaignExist(String campaignId) {
        if (campaignDoesNotExist(campaignId)) {
            throw new EntityNotFoundException(Campaign.class, campaignId);
        }
    }

    private boolean campaignDoesNotExist(String campaignId) {
        return !campaignRepository.existsById(campaignId);
    }

}
