package ai.smartassets.challenge.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CampaignControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void saveCampaign_createsNewCampaign() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/brands/BRAND_0/campaigns")
                        .content("""
                        {
                            "name": "campaign name",
                            "description": "description"
                        }
                        """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("campaign name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brandId").value("BRAND_0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.campaignId").exists())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void saveCampaignToUnexistingBrand_createsNewCampaign() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/brands/unexisting/campaigns")
                        .content("""
                        {
                            "name": "campaign name",
                            "description": "description"
                        }
                        """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void saveCampaign_emptyNameReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/brands/BRAND_0/campaigns")
                        .content("""
                        {
                            "name": "",
                            "description": "description"
                        }
                        """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void findCampaignsByBrandId_returnsListOfCampaigns() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/brands/BRAND_1/campaigns")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(50))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(5L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findCampaignsByBrandIdSendingPage_returnsListOfCampaigns() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/brands/BRAND_1/campaigns")
                        .param("page", "1")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(5L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findCampaignsByUnexistingBrandId_returnsListOfCampaigns() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/brands/unexisting/campaigns")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void findCampaignById_returnsCampaign() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/campaigns/CAMPAIGN_0_0")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Campaign 0 for Brand 0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description for Campaign 0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.campaignId").value("CAMPAIGN_0_0"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findBrandByIdUnexistingBrand_returnsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/campaigns/unexisting")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
