package de.itemis.bonn.rating.persistence.mongo;

import de.itemis.bonn.rating.Rating;
import de.itemis.bonn.rating.spi.RatingPersistenceService;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class MongoRatingPersistenceService implements RatingPersistenceService {

  private final MongoRatingRepository ratingRepository;

  public MongoRatingPersistenceService(final MongoRatingRepository ratingRepository) {
    this.ratingRepository = ratingRepository;
  }

  @Override
  public Rating findRatingById(final String ratingId) {
    final Optional<RatingEntity> entity = ratingRepository.findById(ratingId);
    return entity.map(MongoRatingPersistenceService::mapEntityToModel).orElse(null);
  }

  @Override
  public Rating storeRating(final Rating rating) {
    final RatingEntity entity = new RatingEntity(rating);
    return mapEntityToModel(ratingRepository.save(entity));
  }

  @Override
  public List<Rating> findAllRatings() {
    return ratingRepository.findAll().stream().map(MongoRatingPersistenceService::mapEntityToModel).collect(toList());
  }

  private static Rating mapEntityToModel(final RatingEntity entity) {
    final Rating rating = new Rating();
    rating.setId(entity.getId());
    rating.setValue(entity.getValue());
    return rating;
  }
}
