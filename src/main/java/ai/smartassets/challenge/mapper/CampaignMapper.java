package ai.smartassets.challenge.mapper;

import ai.smartassets.challenge.dto.request.CampaignRequest;
import ai.smartassets.challenge.dto.response.CampaignResponse;
import ai.smartassets.challenge.model.Campaign;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CampaignMapper {

    CampaignMapper INSTANCE = Mappers.getMapper(CampaignMapper.class);

    Campaign campaignRequestToCampaign(CampaignRequest campaignRequest);

    CampaignResponse campaignToCampaignResponse(Campaign campaign);
}
