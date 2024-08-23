package ai.smartassets.challenge.util;

import ai.smartassets.challenge.model.Brand;
import ai.smartassets.challenge.model.Campaign;
import ai.smartassets.challenge.model.Creative;
import ai.smartassets.challenge.repository.BrandRepository;
import ai.smartassets.challenge.repository.CampaignRepository;
import ai.smartassets.challenge.repository.CreativeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Profile("test")
@Slf4j
public class DataGenerator implements CommandLineRunner {

    private final BrandRepository brandRepository;
    private final CampaignRepository campaignRepository;
    private final CreativeRepository creativeRepository;

    @Override
    public void run(String... args) throws Exception {
        generateTestData();
    }

    private void generateTestData() {
        int numberOfBrands = 5;
        int campaignsPerBrand = 5;
        int creativesPerCampaign = 10;

        List<Brand> brands = new ArrayList<>();
        List<Campaign> campaigns = new ArrayList<>();
        List<Creative> creatives = new ArrayList<>();

        IntStream.range(0, numberOfBrands).forEach(i -> {
            Brand brand = Brand.builder().brandId("BRAND_" + i)
                    .name("Brand " + i)
                    .description("Description for Brand " + i)
                    .build();

            brands.add(brand);

            IntStream.range(0, campaignsPerBrand).forEach(j -> {
                Campaign campaign = Campaign.builder()
                        .campaignId("CAMPAIGN_" + i + "_" + j)
                        .name("Campaign " + j + " for Brand " + i)
                        .description("Description for Campaign " + j)
                        .brandId(brand.getBrandId())
                        .build();

                campaigns.add(campaign);

                IntStream.range(0, creativesPerCampaign).forEach(k -> {
                    Creative creative = Creative.builder()
                            .creativeId("CREATIVE_" + i + "_" + j + "_" + k)
                            .name("Creative " + k + " for Campaign " + j)
                            .description("Description for Creative " + k)
                            .creativeUrl("https://cdn.example.com/creatives/creative_" + k + ".jpg")
                            .campaignId(campaign.getCampaignId())
                            .build();

                    creatives.add(creative);

                });
            });
        });

        log.info("Saving test data");

        CompletableFuture<Void> saveBrandsFuture = CompletableFuture.runAsync(() -> brandRepository.saveAll(brands));
        CompletableFuture<Void> saveCreativesFuture = CompletableFuture.runAsync(() -> creativeRepository.saveAll(creatives));
        CompletableFuture<Void> saveCampaignsFuture = CompletableFuture.runAsync(() -> campaignRepository.saveAll(campaigns));

        saveCampaignsFuture.join();
        saveCreativesFuture.join();
        saveBrandsFuture.join();

        log.info("Test data saved successfully");
    }
}
