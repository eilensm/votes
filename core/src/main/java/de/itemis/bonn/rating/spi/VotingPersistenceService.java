package de.itemis.bonn.rating.spi;

import de.itemis.bonn.rating.Item;

import java.util.List;
import java.util.Optional;

public interface VotingPersistenceService {

  Optional<Item> findItemById(String itemId);

  Item storeItem(Item item);

  List<Item> findAllItems();
}
