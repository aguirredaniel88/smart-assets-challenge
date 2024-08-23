package ai.smartassets.challenge.service;

import ai.smartassets.challenge.dto.request.CreativeRequest;
import ai.smartassets.challenge.dto.response.CreativeResponse;
import ai.smartassets.challenge.dto.response.PagedResponse;
import ai.smartassets.challenge.exception.EntityNotFoundException;
import ai.smartassets.challenge.mapper.CreativeMapper;
import ai.smartassets.challenge.mapper.PagedResponseMapper;
import ai.smartassets.challenge.model.Creative;
import ai.smartassets.challenge.repository.CreativeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreativeService {

    private final CreativeRepository creativeRepository;
    private final CampaignService campaignService;

    public CreativeResponse saveCreative(String campaignId, CreativeRequest creativeRequest) {
        campaignService.validateCampaignExist(campaignId);
        Creative creative = CreativeMapper.INSTANCE.creativeRequestToCreative(creativeRequest);

        creative.setCampaignId(campaignId);
        creative.setCreativeId(UUID.randomUUID().toString());

        return CreativeMapper.INSTANCE.creativeToCreativeResponse(creativeRepository.save(creative));

    }

    public PagedResponse<CreativeResponse> findCreativeByCampaignId(String campaignId, Integer page, Integer size) {
        campaignService.validateCampaignExist(campaignId);
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Creative> creatives = creativeRepository.findByCampaignId(campaignId, pageRequest);
        return PagedResponseMapper.INSTANCE.mapPageToPagedResponse(creatives, CreativeMapper.INSTANCE::creativeToCreativeResponse);
    }

    public CreativeResponse findCreativeById(String creativeId) {
        return CreativeMapper.INSTANCE.creativeToCreativeResponse(creativeRepository.findById(creativeId)
                .orElseThrow(() -> new EntityNotFoundException(Creative.class, creativeId)));
    }
}
