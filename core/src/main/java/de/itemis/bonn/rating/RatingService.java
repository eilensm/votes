package de.itemis.bonn.rating;

import de.itemis.bonn.rating.spi.RatingPersistenceService;

import java.util.List;

public class RatingService {

  private final RatingPersistenceService ratingPersistenceService;

  public RatingService(final RatingPersistenceService ratingPersistenceService) {
    this.ratingPersistenceService = ratingPersistenceService;
  }

  public Rating createRating(final int value) {
    final Rating rating = new Rating();
    rating.setValue(value);
    return ratingPersistenceService.storeRating(rating);
  }

  public Rating rate(final Rating rating) {
    final Rating storedRating = ratingPersistenceService.findRatingById(rating.getId());
    if (storedRating == null) {
      throw new RatingException("rating not found");
    }
    storedRating.setValue(rating.getValue());
    return ratingPersistenceService.storeRating(storedRating);
  }

  public Rating getRating(final String ratingId) {
    return ratingPersistenceService.findRatingById(ratingId);
  }

  public List<Rating> getRatings() {
    return ratingPersistenceService.findAllRatings();
  }
}
