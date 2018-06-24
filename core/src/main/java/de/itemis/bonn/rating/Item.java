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

  void calcAverage() {
    if (votes != null) {
      final int totalVotesCount = votes.stream().mapToInt(Vote::getVoteCount).sum();
      final double ratings = votes.stream()
          .mapToDouble(x -> x.getRating() * x.getVoteCount())
          .sum();
      average = ratings / totalVotesCount;
    }
  }
}
