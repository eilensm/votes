package de.itemis.bonn.rating.persistence.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoItemRepository extends MongoRepository<ItemEntity, String> {
}
