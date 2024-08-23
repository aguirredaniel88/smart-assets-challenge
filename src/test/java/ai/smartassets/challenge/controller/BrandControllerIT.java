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
class BrandControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void saveBrand_createsNewBrand() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/brands")
                .content("""
                        {
                            "name": "brand",
                            "description": "description"
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("brand"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brandId").exists())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void saveBrand_emptyBrandNameReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/brands")
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
    void findAllBrands_returnsListOfBrands() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/brands")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(50))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findAllBrandsSendingPage_returnsListOfBrands() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/brands")
                        .param("page", "0")
                        .param("size", "4")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findBrandById_returnsBrand() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/brands/BRAND_0")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Brand 0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description for Brand 0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brandId").value("BRAND_0"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findBrandByIdUnexistingBrand_returnsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/brands/unexisting")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
