package de.itemis.bonn.rating.persistence.mongo;

import de.itemis.bonn.rating.Item;
import de.itemis.bonn.rating.Vote;
import de.itemis.bonn.rating.spi.VotingPersistenceService;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class MongoVotingPersistenceService implements VotingPersistenceService {

  private final MongoItemRepository mongoItemRepository;

  public MongoVotingPersistenceService(final MongoItemRepository mongoItemRepository) {
    this.mongoItemRepository = mongoItemRepository;
  }

  @Override
  public Item findItemById(final String itemId) {
    final Optional<ItemEntity> entity = mongoItemRepository.findById(itemId);
    return entity.map(MongoVotingPersistenceService::mapEntityToModel).orElse(null);
  }

  @Override
  public Item storeItem(final Item item) {
    final ItemEntity entity = mapModelToEntity(item);
    return mapEntityToModel(mongoItemRepository.save(entity));
  }

  @Override
  public List<Item> findAllItems() {
    return mongoItemRepository.findAll()
        .stream()
        .map(MongoVotingPersistenceService::mapEntityToModel)
        .collect(toList());
  }

  private static ItemEntity mapModelToEntity(final Item item) {
    return ItemEntity.builder()
        .id(item.getId())
        .description(item.getDescription())
        .votes(item.getVotes() == null ? null :
            item.getVotes().stream().map(x -> VoteEntity.builder()
                .id(x.getId())
                .voteCount(x.getVoteCount())
                .build())
                .collect(toList()))
        .build();
  }

  private static Item mapEntityToModel(final ItemEntity entity) {
    final Item item = Item.builder()
        .id(entity.getId())
        .description(entity.getDescription())
        .build();
    item.setVotes(new ArrayList<>());

    if (entity.getVotes() != null && !entity.getVotes().isEmpty()) {
      item.setVotes(entity.getVotes()
          .stream()
          .map(x -> Vote.builder().id(x.getId() == null ? new ObjectId().toString() : x.getId()).voteCount(x.getVoteCount()).build())
          .collect(toList()));
    }
    return item;
  }
}
