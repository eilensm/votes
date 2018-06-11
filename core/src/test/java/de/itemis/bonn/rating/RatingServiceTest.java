package de.itemis.bonn.rating;

import de.itemis.bonn.rating.spi.RatingPersistenceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RatingServiceTest {

  private static final int RATING_VALUE = 4711;
  private static final String RATING_ID = "89xosdfjew0";
  private static final int RATING2_VALUE = 4545;
  private static final String RATING2_ID = "Bxj8908de";

  @Mock
  private RatingPersistenceService ratingPersistenceService;

  @InjectMocks
  private RatingService ratingService;

  @Before
  public void setUp() {
    when(ratingPersistenceService.storeRating(any(Rating.class))).then(invocation -> {
      final Rating input = invocation.getArgument(0);
      final Rating answer = new Rating();
      answer.setId(input.getId() != null ? input.getId() : RATING_ID);
      answer.setValue(input.getValue());
      return answer;
    });
    when(ratingPersistenceService.findRatingById(RATING_ID)).thenReturn(buildRating(RATING_ID, RATING_VALUE));
    when(ratingPersistenceService.findAllRatings()).thenReturn(
        asList(buildRating(RATING_ID, RATING_VALUE), buildRating(RATING2_ID, RATING2_VALUE)));
  }

  @Test
  public void createRatingShouldStoreRating() {
    ratingService.createRating(RATING_VALUE);
    verify(ratingPersistenceService).storeRating(any(Rating.class));
  }

  @Test
  public void createRatingShouldReturnPersistedRating() {
    final Rating createdRating = ratingService.createRating(RATING_VALUE);
    final ArgumentCaptor<Rating> captor = ArgumentCaptor.forClass(Rating.class);
    assertThat(createdRating.getId()).isNotNull();
    assertThat(captor.getValue().getValue()).isEqualTo(RATING_VALUE);
  }

  @Test
  public void rateShouldThrowIfRatingDoesNotExist() {
    assertThatThrownBy(() -> ratingService.rate(mock(Rating.class)))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("rating not found");
  }

  @Test
  public void rateShouldPersistNewValue() {
    final int newValue = 666;
    final Rating ratingToUpdate = buildRating(RATING_ID, newValue);
    ratingService.rate(ratingToUpdate);
    final ArgumentCaptor<Rating> captor = ArgumentCaptor.forClass(Rating.class);
    verify(ratingPersistenceService).storeRating(captor.capture());
    assertThat(captor.getValue().getValue()).isEqualTo(newValue);
  }

  @Test
  public void rateShouldReturnUpdatedRating() {
    final int newValue = 666;
    final Rating ratingToUpdate = buildRating(RATING_ID, newValue);
    final Rating updatedRating = ratingService.rate(ratingToUpdate);
    assertThat(updatedRating.getValue()).isEqualTo(newValue);
  }

  @Test
  public void getRatingShouldQueryPersistence() {
    ratingService.getRating(RATING_ID);
    verify(ratingPersistenceService).findRatingById(RATING_ID);
  }

  @Test
  public void getRatingShouldReturnTheRating() {
    final Rating rating = ratingService.getRating(RATING_ID);
    assertThat(rating).isNotNull();
    assertThat(rating.getId()).isEqualTo(RATING_ID);
    assertThat(rating.getValue()).isEqualTo(RATING_VALUE);
  }

  @Test
  public void getRatingShouldReturnNullIfNotFound() {
    when(ratingPersistenceService.findRatingById(RATING_ID)).thenReturn(null);
    assertThat(ratingService.getRating(RATING_ID)).isNull();
  }

  @Test
  public void getRatingsShouldQueryPersistence() {
    final List<Rating> ratings = ratingService.getRatings();
    verify(ratingPersistenceService).findAllRatings();
  }

  @Test
  public void getRatingsShouldReturnAllExistingRatings() {
    final List<Rating> ratings = ratingService.getRatings();
    assertThat(ratings)
        .extracting(Rating::getId, Rating::getValue)
        .containsExactly(
            tuple(RATING_ID, RATING_VALUE),
            tuple(RATING2_ID, RATING2_VALUE));
  }

  private static Rating buildRating(final String id, final int value) {
    final Rating persistedRating = new Rating();
    persistedRating.setValue(value);
    persistedRating.setId(id);
    return persistedRating;
  }
}
