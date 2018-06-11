package de.itemis.bonn.rating.persistence.mongo;

import de.itemis.bonn.rating.Rating;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rating")
public class RatingEntity {

  private String id;

  private int value;

  public RatingEntity() {
  }

  public RatingEntity(final Rating rating) {
    id = rating.getId();
    value = rating.getValue();
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public int getValue() {
    return value;
  }

  public void setValue(final int value) {
    this.value = value;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    final RatingEntity entity = (RatingEntity) o;

    return new EqualsBuilder()
        .append(value, entity.value)
        .append(id, entity.id)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(id)
        .append(value)
        .toHashCode();
  }
}
