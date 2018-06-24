package de.itemis.bonn.rating.persistence.mongo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder(toBuilder = true)
public class VoteEntity {
  @Id
  private int rating;
  private int voteCount;
}
