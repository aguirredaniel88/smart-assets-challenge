package ai.smartassets.challenge.service;

import ai.smartassets.challenge.dto.request.CampaignRequest;
import ai.smartassets.challenge.dto.response.BrandResponse;
import ai.smartassets.challenge.dto.response.CampaignResponse;
import ai.smartassets.challenge.dto.response.PagedResponse;
import ai.smartassets.challenge.exception.EntityNotFoundException;
import ai.smartassets.challenge.model.Campaign;
import ai.smartassets.challenge.repository.CampaignRepository;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CampaignServiceTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private BrandService brandService;

    @Captor
    private ArgumentCaptor<Campaign> campaignArgumentCaptor;

    @InjectMocks
    private CampaignService campaignService;

    @Test
    void saveCampaign_campaignIdGeneratedSuccessfully() {
        String brandId = "brandId";
        String campaignName = "Campaign 1";
        String campaignDescription = "Campaign Description";
        String campaignId = UUID.randomUUID().toString();
        Campaign campaign = Campaign.builder()
                .campaignId(campaignId)
                .brandId(brandId)
                .description(campaignDescription)
                .name(campaignName)
                .build();

        CampaignRequest campaignRequest = new CampaignRequest(campaignName, campaignDescription);

        CampaignResponse expectedResponse = new CampaignResponse(campaign.getCampaignId(), campaignName, campaignDescription, brandId);

        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);

        CampaignResponse actualResponse = campaignService.saveCampaign(brandId, campaignRequest);

        verify(brandService).validateBrandExists(brandId);
        verify(campaignRepository).save(campaignArgumentCaptor.capture());

        Campaign actualCampaignToSave = campaignArgumentCaptor.getValue();

        assertThat(actualCampaignToSave.getCampaignId()).isNotBlank();
        assertThat(actualCampaignToSave.getName()).isEqualTo(campaignName);
        assertThat(actualCampaignToSave.getBrandId()).isEqualTo(brandId);
        assertThat(actualCampaignToSave.getDescription()).isEqualTo(campaignDescription);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void findCampaignsByBrandId_campaignsFoundSuccessfully() {
        String brandId = "brandId";
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Campaign> campaignPage = mock(Page.class);
        String campaignName = "Campaign 1";
        String campaignDescription = "Campaign Description";
        String campaignId = UUID.randomUUID().toString();
        Campaign campaign = Campaign.builder()
                .campaignId(campaignId)
                .brandId(brandId)
                .description(campaignDescription)
                .name(campaignName)
                .build();

        PagedResponse<CampaignResponse> expectedResponse = new PagedResponse<>(
                List.of(new CampaignResponse(campaignId, campaignName, campaignDescription, brandId)),
                page,
                size,
                1L,
                1,
                true);
        when(campaignPage.getTotalElements()).thenReturn(1L);
        when(campaignPage.getTotalPages()).thenReturn(1);
        when(campaignPage.getContent()).thenReturn(List.of(campaign));
        when(campaignPage.isLast()).thenReturn(true);
        when(campaignPage.getSize()).thenReturn(size);
        when(campaignPage.getNumber()).thenReturn(page);
        when(campaignRepository.findByBrandId(brandId, pageRequest)).thenReturn(campaignPage);

        PagedResponse<CampaignResponse> actualResponse = campaignService.findCampaignsByBrandId(brandId, page, size);

        verify(brandService).validateBrandExists(brandId);
        verify(campaignRepository).findByBrandId(brandId, pageRequest);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void findCampaignsByBrandId_emptyPage_noCampaignsFound() {
        String brandId = "brandId";
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Campaign> campaignPage = Page.empty(pageRequest);
        PagedResponse<CampaignResponse> expectedResponse = new PagedResponse<>(List.of(), page, size, 0L, 0, true);

        when(campaignRepository.findByBrandId(brandId, pageRequest)).thenReturn(campaignPage);

        PagedResponse<CampaignResponse> actualResponse = campaignService.findCampaignsByBrandId(brandId, page, size);

        verify(brandService).validateBrandExists(brandId);
        verify(campaignRepository).findByBrandId(brandId, pageRequest);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void findCampaignById_campaignDoesNotExist_throwsException() {
        String invalidCampaignId = UUID.randomUUID().toString();
        when(campaignRepository.findById(invalidCampaignId)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> campaignService.findCampaignById(invalidCampaignId));
    }

    @Test
    void validateCampaignExists_campaignExists_noExceptionThrown() {
        String campaignId = UUID.randomUUID().toString();
        when(campaignRepository.existsById(campaignId)).thenReturn(true);

        campaignService.validateCampaignExist(campaignId);

        verify(campaignRepository).existsById(campaignId);
    }

    @Test
    void validateCampaignExists_campaignDoesNotExist_throwsException() {
        String campaignId = UUID.randomUUID().toString();
        when(campaignRepository.existsById(campaignId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> campaignService.validateCampaignExist(campaignId));
    }

}
