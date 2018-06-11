package de.itemis.bonn.rating;

public class RatingException extends RuntimeException {
  public RatingException() {
  }

  public RatingException(final String message) {
    super(message);
  }

  public RatingException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public RatingException(final Throwable cause) {
    super(cause);
  }

  public RatingException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
