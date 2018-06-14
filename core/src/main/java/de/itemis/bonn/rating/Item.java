package de.itemis.bonn.rating;

import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Item {
  private String id;
  private String description;
  @Singular
  private List<Vote> votes;
  private double average;

  public void calcAverage() {
    votes.stream()
        .mapToDouble(Vote::getVoteCount)
        .average()
        .ifPresent(x -> this.average = x);
  }
}
