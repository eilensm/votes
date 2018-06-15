package de.itemis.bonn.rating;

import de.itemis.bonn.rating.spi.VotingPersistenceService;

import java.util.List;
import java.util.Optional;

public class VotingService {

  private final VotingPersistenceService votingPersistenceService;

  public VotingService(final VotingPersistenceService votingPersistenceService) {
    this.votingPersistenceService = votingPersistenceService;
  }

  public Item createItem(final String description) {
    final Item vote = Item.builder()
        .description(description)
        .build();
    return votingPersistenceService.storeItem(vote);
  }

  public Item getItem(final String itemId) {
    final Item item = votingPersistenceService.findItemById(itemId);
    if (item != null) {
      item.calcAverage();
    }
    return item;
  }

  public List<Item> getItems() {
    final List<Item> allItems = votingPersistenceService.findAllItems();
    allItems.forEach(Item::calcAverage);
    return allItems;
  }

  public Item vote(final String itemId, final Vote vote) {
    final Item item = votingPersistenceService.findItemById(itemId);
    if (vote.getId() == null) {
      item.getVotes().add(vote);
    } else {
      final Optional<Vote> existingVote = item.getVotes().stream().filter(x -> x.getId().equals(vote.getId())).findFirst();
      existingVote.ifPresent(x -> x.setVoteCount(x.getVoteCount() + 1));
    }
    final Item storedItem = votingPersistenceService.storeItem(item);
    storedItem.calcAverage();
    return storedItem;
  }
}
