package ai.smartassets.challenge.repository;

import ai.smartassets.challenge.model.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrandRepository extends MongoRepository<Brand, String> {
}
