package de.itemis.bonn.rating.spi;

import de.itemis.bonn.rating.Item;

import java.util.List;

public interface VotingPersistenceService {

  Item findItemById(String itemId);

  Item storeItem(Item item);

  List<Item> findAllItems();
}
