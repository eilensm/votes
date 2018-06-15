package de.itemis.bonn.rating;

import de.itemis.bonn.rating.spi.VotingPersistenceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
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
    when(votingPersistenceService.findItemById(ITEM_ID)).thenReturn(buildItem(ITEM_ID, DESCRIPTION));
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
  public void getRatingShouldReturnNullIfNotFound() {
    when(votingPersistenceService.findItemById(ITEM_ID)).thenReturn(null);
    assertThat(votingService.getItem(ITEM_ID)).isNull();
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

  private static Item buildItem(final String id, final String description) {
    final Item item = new Item();
    item.setDescription(description);
    item.setId(id);
    return item;
  }
}
