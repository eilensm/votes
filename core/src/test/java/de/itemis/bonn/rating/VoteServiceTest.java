//package de.itemis.bonn.rating;
//
//import de.itemis.bonn.rating.spi.VotingPersistenceService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.List;
//
//import static java.util.Arrays.asList;
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class VoteServiceTest {
//
//  private static final int RATING_VALUE = 4711;
//  private static final String RATING_ID = "89xosdfjew0";
//  private static final int RATING2_VALUE = 4545;
//  private static final String RATING2_ID = "Bxj8908de";
//
//  @Mock
//  private VotingPersistenceService votingPersistenceService;
//
//  @InjectMocks
//  private VotingService votingService;
//
//  @Before
//  public void setUp() {
//    when(votingPersistenceService.storeItem(any(Vote.class))).then(invocation -> {
//      final Vote input = invocation.getArgument(0);
//      final Vote answer = new Vote();
//      answer.setId(input.getId() != null ? input.getId() : RATING_ID);
//      answer.setVoteCount(input.getVoteCount());
//      return answer;
//    });
//    when(votingPersistenceService.findItemById(RATING_ID)).thenReturn(buildRating(RATING_ID, RATING_VALUE));
//    when(votingPersistenceService.findAllItems()).thenReturn(
//        asList(buildRating(RATING_ID, RATING_VALUE), buildRating(RATING2_ID, RATING2_VALUE)));
//  }
//
//  @Test
//  public void createRatingShouldStoreRating() {
//    votingService.createItem(RATING_VALUE);
//    verify(votingPersistenceService).storeItem(any(Vote.class));
//  }
//
//  @Test
//  public void createRatingShouldReturnPersistedRating() {
//    final Vote createdVote = votingService.createItem(RATING_VALUE);
//    final ArgumentCaptor<Vote> captor = ArgumentCaptor.forClass(Vote.class);
//    assertThat(createdVote.getId()).isNotNull();
//    assertThat(captor.getValue().getVoteCount()).isEqualTo(RATING_VALUE);
//  }
//
//  @Test
//  public void rateShouldThrowIfRatingDoesNotExist() {
//    assertThatThrownBy(() -> votingService.rate(mock(Vote.class)))
//        .isInstanceOf(RuntimeException.class)
//        .hasMessage("rating not found");
//  }
//
//  @Test
//  public void rateShouldPersistNewValue() {
//    final int newValue = 666;
//    final Vote voteToUpdate = buildRating(RATING_ID, newValue);
//    votingService.rate(voteToUpdate);
//    final ArgumentCaptor<Vote> captor = ArgumentCaptor.forClass(Vote.class);
//    verify(votingPersistenceService).storeItem(captor.capture());
//    assertThat(captor.getValue().getVoteCount()).isEqualTo(newValue);
//  }
//
//  @Test
//  public void rateShouldReturnUpdatedRating() {
//    final int newValue = 666;
//    final Vote voteToUpdate = buildRating(RATING_ID, newValue);
//    final Vote updatedVote = votingService.rate(voteToUpdate);
//    assertThat(updatedVote.getVoteCount()).isEqualTo(newValue);
//  }
//
//  @Test
//  public void getRatingShouldQueryPersistence() {
//    votingService.getItem(RATING_ID);
//    verify(votingPersistenceService).findItemById(RATING_ID);
//  }
//
//  @Test
//  public void getRatingShouldReturnTheRating() {
//    final Vote vote = votingService.getItem(RATING_ID);
//    assertThat(vote).isNotNull();
//    assertThat(vote.getId()).isEqualTo(RATING_ID);
//    assertThat(vote.getVoteCount()).isEqualTo(RATING_VALUE);
//  }
//
//  @Test
//  public void getRatingShouldReturnNullIfNotFound() {
//    when(votingPersistenceService.findItemById(RATING_ID)).thenReturn(null);
//    assertThat(votingService.getItem(RATING_ID)).isNull();
//  }
//
//  @Test
//  public void getRatingsShouldQueryPersistence() {
//    final List<Vote> votes = votingService.getRatings();
//    verify(votingPersistenceService).findAllItems();
//  }
//
//  @Test
//  public void getRatingsShouldReturnAllExistingRatings() {
//    final List<Vote> votes = votingService.getRatings();
//    assertThat(votes)
//        .extracting(Vote::getId, Vote::getVoteCount)
//        .containsExactly(
//            tuple(RATING_ID, RATING_VALUE),
//            tuple(RATING2_ID, RATING2_VALUE));
//  }
//
//  private static Vote buildRating(final String id, final int value) {
//    final Vote persistedVote = new Vote();
//    persistedVote.setVoteCount(value);
//    persistedVote.setId(id);
//    return persistedVote;
//  }
//}
