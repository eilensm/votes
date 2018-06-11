package de.itemis.bonn.rating;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Rating {

  private String id;
  private int value;

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

    final Rating rating = (Rating) o;

    return new EqualsBuilder()
        .append(value, rating.value)
        .append(id, rating.id)
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
