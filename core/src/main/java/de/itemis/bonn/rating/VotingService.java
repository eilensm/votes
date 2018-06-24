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
    final Item item = votingPersistenceService.findItemById(itemId)
        .orElseThrow(() -> new VotingException("item not found"));
    item.calcAverage();
    return item;
  }

  public List<Item> getItems() {
    final List<Item> allItems = votingPersistenceService.findAllItems();
    allItems.forEach(Item::calcAverage);
    return allItems;
  }

  public Item vote(final String itemId, final Vote vote) {
    final Item item = votingPersistenceService.findItemById(itemId)
        .orElseThrow(() -> new VotingException("item not " + "found"));
    final Optional<Vote> existingVote = item.getVotes()
        .stream()
        .filter(x -> x.getRating() == vote.getRating())
        .findFirst();
    if (existingVote.isPresent()) {
      existingVote.get().setVoteCount(existingVote.get().getVoteCount() + 1);
    } else {
      vote.setVoteCount(1);
      item.getVotes().add(vote);
    }
    final Item storedItem = votingPersistenceService.storeItem(item);
    storedItem.calcAverage();
    return storedItem;
  }
}
