package de.itemis.bonn.rating.spi;

import de.itemis.bonn.rating.Rating;

import java.util.List;

public interface RatingPersistenceService {

  Rating findRatingById(String ratingId);

  Rating storeRating(Rating rating);

  List<Rating> findAllRatings();
}
