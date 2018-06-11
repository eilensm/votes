package de.itemis.bonn.rating.persistence.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoRatingRepository extends MongoRepository<RatingEntity, String> {
}
