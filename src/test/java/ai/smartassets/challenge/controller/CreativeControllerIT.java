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
public class CreativeControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void saveCreative_createsNewCreative() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/campaigns/CAMPAIGN_0_0/creatives")
                        .content("""
                        {
                            "name": "creative name",
                            "creativeUrl": "http://fakeurl.com",
                            "description": "description"
                        }
                        """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("creative name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creativeUrl").value("http://fakeurl.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.campaignId").value("CAMPAIGN_0_0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creativeId").exists())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void saveCreativeToUnexistingCampaign_createsNewCreative() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/campaigns/unexisting/creatives")
                        .content("""
                        {
                            "name": "creative name",
                            "creativeUrl": "http://fakeurl.com",
                            "description": "description"
                        }
                        """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void saveCreative_emptyNameReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/campaigns/unexisting/creatives")
                        .content("""
                        {
                            "name": "",
                            "creativeUrl": "http://fakeurl.com",
                            "description": "description"
                        }
                        """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveCreative_emptyCreativeUrlReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/campaigns/unexisting/creatives")
                        .content("""
                        {
                            "name": "creative name",
                            "creativeUrl": "",
                            "description": "description"
                        }
                        """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void findCreativesByCampaignIdSendingPage_returnsListOfCreatives() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/campaigns/CAMPAIGN_0_1/creatives")
                        .param("page", "0")
                        .param("size", "5")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(10L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findCreativesByCampaignId_returnsListOfCreatives() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/campaigns/CAMPAIGN_0_1/creatives")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(50))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(10L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findCampaignsByUnexistingBrandId_returnsListOfCampaigns() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/campaigns/unexisting/creatives")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void findCampaignById_returnsCampaign() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/creatives/CREATIVE_0_0_0")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Creative 0 for Campaign 0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description for Creative 0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creativeUrl").value("https://cdn.example.com/creatives/creative_0.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.campaignId").value("CAMPAIGN_0_0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creativeId").value("CREATIVE_0_0_0"))
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
