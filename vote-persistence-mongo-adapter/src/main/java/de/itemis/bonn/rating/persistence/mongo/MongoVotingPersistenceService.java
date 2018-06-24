package de.itemis.bonn.rating.persistence.mongo;

import de.itemis.bonn.rating.Item;
import de.itemis.bonn.rating.Vote;
import de.itemis.bonn.rating.spi.VotingPersistenceService;

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
  public Optional<Item> findItemById(final String itemId) {
    return mongoItemRepository.findById(itemId).map(MongoVotingPersistenceService::mapEntityToModel);
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
                .rating(x.getRating())
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
          .map(x -> Vote.builder().rating(x.getRating()).voteCount(x.getVoteCount()).build())
          .collect(toList()));
    }
    return item;
  }
}
