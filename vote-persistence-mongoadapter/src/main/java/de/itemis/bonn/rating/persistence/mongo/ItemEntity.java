package de.itemis.bonn.rating.persistence.mongo;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "items")
@Data
@Builder(toBuilder = true)
public class ItemEntity {
  private String id;
  private String description;
  @Singular
  private List<VoteEntity> votes;
}
