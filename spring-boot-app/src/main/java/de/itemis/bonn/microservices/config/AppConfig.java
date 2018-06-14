package de.itemis.bonn.microservices.config;

import de.itemis.bonn.rating.VotingService;
import de.itemis.bonn.rating.persistence.mongo.MongoItemRepository;
import de.itemis.bonn.rating.persistence.mongo.MongoVotingPersistenceService;
import de.itemis.bonn.rating.spi.VotingPersistenceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"de.itemis.bonn.rating.persistence.mongo"})
public class AppConfig {

  @Bean
  public VotingPersistenceService ratingPersistenceService(final MongoItemRepository ratingRepository) {
    return new MongoVotingPersistenceService(ratingRepository);
  }

  @Bean
  public VotingService ratingService(final VotingPersistenceService votingPersistenceService) {
    return new VotingService(votingPersistenceService);
  }
}
