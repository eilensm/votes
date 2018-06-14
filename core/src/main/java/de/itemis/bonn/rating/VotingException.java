package de.itemis.bonn.rating;

public class VotingException extends RuntimeException {
  public VotingException() {
  }

  public VotingException(final String message) {
    super(message);
  }

  public VotingException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public VotingException(final Throwable cause) {
    super(cause);
  }

  public VotingException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
