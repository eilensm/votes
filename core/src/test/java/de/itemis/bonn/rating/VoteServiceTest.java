package de.itemis.bonn.rating;

import de.itemis.bonn.rating.spi.VotingPersistenceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VoteServiceTest {

  private static final String ITEM_ID = "89xosdfjew0";
  private static final String DESCRIPTION = "item-1";
  private static final String ITEM2_ID = "Bxj8908de";
  private static final String DESCRIPTION2 = "item-2";

  @Mock
  private VotingPersistenceService votingPersistenceService;

  @InjectMocks
  private VotingService votingService;

  @Before
  public void setUp() {
    when(votingPersistenceService.storeItem(any(Item.class))).then(invocation -> {
      final Item input = invocation.getArgument(0);
      final Item answer = new Item();
      answer.setId(input.getId() != null ? input.getId() : ITEM_ID);
      answer.setDescription(input.getDescription());
      answer.setVotes(input.getVotes());
      return answer;
    });
    when(votingPersistenceService.findItemById(ITEM_ID)).thenReturn(Optional.of(buildItem(ITEM_ID, DESCRIPTION)));
    when(votingPersistenceService.findAllItems()).thenReturn(
        asList(buildItem(ITEM_ID, DESCRIPTION), buildItem(ITEM2_ID, DESCRIPTION2)));
  }

  @Test
  public void createItemShouldStoreItem() {
    votingService.createItem(DESCRIPTION);
    verify(votingPersistenceService).storeItem(any(Item.class));
  }

  @Test
  public void createItemShouldReturnPersistedItem() {
    final Item createdItem = votingService.createItem(DESCRIPTION);
    assertThat(createdItem.getId()).isNotNull();
    assertThat(createdItem.getDescription()).isEqualTo(DESCRIPTION);
  }

  @Test
  public void getItemShouldQueryPersistence() {
    votingService.getItem(ITEM_ID);
    verify(votingPersistenceService).findItemById(ITEM_ID);
  }

  @Test
  public void getItemShouldReturnTheItem() {
    final Item item = votingService.getItem(ITEM_ID);
    assertThat(item).isNotNull();
    assertThat(item.getId()).isEqualTo(ITEM_ID);
    assertThat(item.getDescription()).isEqualTo(DESCRIPTION);
  }

  @Test
  public void getItemShouldThrowIfNotFound() {
    when(votingPersistenceService.findItemById(ITEM_ID)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> votingService.getItem(ITEM_ID))
        .isInstanceOf(VotingException.class)
        .hasMessage("item not found");
  }

  @Test
  public void getItemsShouldQueryPersistence() {
    votingService.getItems();
    verify(votingPersistenceService).findAllItems();
  }

  @Test
  public void getItemsShouldReturnAllExistingItems() {
    final List<Item> items = votingService.getItems();
    assertThat(items)
        .extracting(Item::getId, Item::getDescription)
        .containsExactly(
            tuple(ITEM_ID, DESCRIPTION),
            tuple(ITEM2_ID, DESCRIPTION2));
  }

  @Test
  public void voteShouldAddNewVote() {
    final int voteCount = 5;
    final Vote vote = Vote.builder().voteCount(voteCount).build();
    final Item item = votingService.vote(ITEM_ID, vote);
    assertThat(item.getVotes()).containsExactly(vote);
  }

  @Test
  public void voteShouldUpdateExistingVote() {
    final int voteCount = 5;
    final Vote vote = Vote.builder()
        .voteCount(voteCount)
        .build();
    final Vote persistedVote = Vote.builder()
        .voteCount(voteCount)
        .build();
    final Item persistedItem = buildItem(ITEM_ID, DESCRIPTION);
    persistedItem.getVotes().add(persistedVote);
    when(votingPersistenceService.findItemById(ITEM_ID)).thenReturn(Optional.of(persistedItem));
    final Item item = votingService.vote(ITEM_ID, vote);
    assertThat(item.getVotes())
        .extracting(Vote::getVoteCount)
        .containsExactly(voteCount + 1);
  }

  @Test
  public void voteShouldUpdateItemWithVote() {
    final Vote vote = Vote.builder().rating(1).build();
    votingService.vote(ITEM_ID, vote);
    final ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
    verify(votingPersistenceService).storeItem(captor.capture());
    assertThat(captor.getValue().getVotes())
        .extracting(Vote::getVoteCount)
        .containsExactly(1);
  }

  @Test
  public void voteShouldCalcNewAverage() {
    final Vote vote = Vote.builder().rating(5).build();
    final Item item = votingService.vote(ITEM_ID, vote);
    assertThat(item.getAverage()).isEqualTo(5d);
  }

  private static Item buildItem(final String id, final String description) {
    final Item item = new Item();
    item.setDescription(description);
    item.setId(id);
    item.setVotes(new ArrayList<>());
    return item;
  }
}
