package de.itemis.bonn.microservices.config;

import de.itemis.bonn.rating.RatingService;
import de.itemis.bonn.rating.persistence.mongo.MongoRatingPersistenceService;
import de.itemis.bonn.rating.persistence.mongo.MongoRatingRepository;
import de.itemis.bonn.rating.spi.RatingPersistenceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"de.itemis.bonn.rating.persistence.mongo"})
public class AppConfig {

  @Bean
  public RatingPersistenceService ratingPersistenceService(final MongoRatingRepository ratingRepository) {
    return new MongoRatingPersistenceService(ratingRepository);
  }

  @Bean
  public RatingService ratingService(final RatingPersistenceService ratingPersistenceService) {
    return new RatingService(ratingPersistenceService);
  }
}
