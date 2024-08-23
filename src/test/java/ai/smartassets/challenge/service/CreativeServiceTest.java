package ai.smartassets.challenge.service;

import ai.smartassets.challenge.dto.request.CreativeRequest;
import ai.smartassets.challenge.dto.response.CampaignResponse;
import ai.smartassets.challenge.dto.response.CreativeResponse;
import ai.smartassets.challenge.dto.response.PagedResponse;
import ai.smartassets.challenge.exception.EntityNotFoundException;
import ai.smartassets.challenge.mapper.CreativeMapper;
import ai.smartassets.challenge.mapper.PagedResponseMapper;
import ai.smartassets.challenge.model.Creative;
import ai.smartassets.challenge.repository.CreativeRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreativeServiceTest {

    @Mock
    private CreativeRepository creativeRepository;

    @Mock
    private CampaignService campaignService;

    @InjectMocks
    private CreativeService creativeService;

    @Captor
    private ArgumentCaptor<Creative> creativeArgumentCaptor;

    @Test
    void saveCreative_creativeSavedSuccessfully() {
        String campaignId = "campaignId";
        String creativeName = "Creative name 1";
        String creativeDescription = "Creative Description";
        String creativeUrl = "http://fakeurl.com";

        String creativeId = UUID.randomUUID().toString();
        Creative creative = Creative.builder()
                .creativeUrl(creativeUrl)
                .creativeId(creativeId)
                .name(creativeName)
                .description(creativeDescription)
                .campaignId(campaignId)
                .build();

        CreativeRequest creativeRequest = new CreativeRequest(creativeName, creativeDescription, creativeUrl);

        CreativeResponse expectedResponse = new CreativeResponse(creative.getCreativeId(), creative.getName(),
                creative.getDescription(), creative.getCreativeUrl(), creative.getCampaignId());

        when(creativeRepository.save(any(Creative.class))).thenReturn(creative);

        CreativeResponse actualResponse = creativeService.saveCreative(campaignId, creativeRequest);

        verify(campaignService).validateCampaignExist(campaignId);
        verify(creativeRepository).save(creativeArgumentCaptor.capture());

        Creative actualCreativeToSave = creativeArgumentCaptor.getValue();

        assertThat(actualCreativeToSave.getCreativeId()).isNotBlank();
        assertThat(actualCreativeToSave.getCreativeUrl()).isEqualTo(creativeUrl);
        assertThat(actualCreativeToSave.getName()).isEqualTo(creativeName);
        assertThat(actualCreativeToSave.getCampaignId()).isEqualTo(campaignId);
        assertThat(actualCreativeToSave.getDescription()).isEqualTo(creativeDescription);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void findCreativeByCampaignId_creativesFoundSuccessfully() {
        String campaignId = "campaignId";
        int page = 0;
        int size = 10;
        String creativeName = "Creative name 1";
        String creativeDescription = "Creative Description";
        String creativeUrl = "http://fakeurl.com";

        String creativeId = UUID.randomUUID().toString();
        Creative creative = Creative.builder()
                .creativeUrl(creativeUrl)
                .creativeId(creativeId)
                .name(creativeName)
                .description(creativeDescription)
                .campaignId(campaignId)
                .build();
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Creative> creativePage = mock(Page.class);
        PagedResponse<CreativeResponse> expectedResponse = new PagedResponse<>(
                List.of(new CreativeResponse(creative.getCreativeId(), creative.getName(),
                        creative.getDescription(), creative.getCreativeUrl(), creative.getCampaignId())),
                page,
                size,
                1L,
                1,
                true);

        when(creativePage.getTotalElements()).thenReturn(1L);
        when(creativePage.getTotalPages()).thenReturn(1);
        when(creativePage.getContent()).thenReturn(List.of(creative));
        when(creativePage.isLast()).thenReturn(true);
        when(creativePage.getSize()).thenReturn(size);
        when(creativePage.getNumber()).thenReturn(page);

        when(creativeRepository.findByCampaignId(campaignId, pageRequest)).thenReturn(creativePage);

        PagedResponse<CreativeResponse> actualResponse = creativeService.findCreativeByCampaignId(campaignId, page, size);

        verify(campaignService).validateCampaignExist(campaignId);
        verify(creativeRepository).findByCampaignId(campaignId, pageRequest);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void findCreativeByCampaignId_emptyPage_noCreativesFound() {
        String campaignId = "campaignId";
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Creative> creativePage = Page.empty(pageRequest);
        PagedResponse<CreativeResponse> expectedResponse = new PagedResponse<>(List.of(), page, size, 0L, 0, true);

        when(creativeRepository.findByCampaignId(campaignId, pageRequest)).thenReturn(creativePage);

        PagedResponse<CreativeResponse> actualResponse = creativeService.findCreativeByCampaignId(campaignId, page, size);

        verify(campaignService).validateCampaignExist(campaignId);
        verify(creativeRepository).findByCampaignId(campaignId, pageRequest);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void findCreativeById_creativeFoundSuccessfully() {
        String campaignId = "campaignId";
        String creativeName = "Creative name 1";
        String creativeDescription = "Creative Description";
        String creativeUrl = "http://fakeurl.com";
        String creativeId = UUID.randomUUID().toString();

        Creative creative = Creative.builder()
                .creativeUrl(creativeUrl)
                .creativeId(creativeId)
                .name(creativeName)
                .description(creativeDescription)
                .campaignId(campaignId)
                .build();

        CreativeResponse expectedResponse = new CreativeResponse(creative.getCreativeId(), creative.getName(),
                creative.getDescription(), creative.getCreativeUrl(), creative.getCampaignId());

        when(creativeRepository.findById(creativeId)).thenReturn(Optional.of(creative));

        CreativeResponse actualResponse = creativeService.findCreativeById(creativeId);

        verify(creativeRepository).findById(creativeId);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void findCreativeById_creativeDoesNotExist_throwsException() {
        String creativeId = "invalidCreativeId";

        when(creativeRepository.findById(creativeId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> creativeService.findCreativeById(creativeId));
    }

}
