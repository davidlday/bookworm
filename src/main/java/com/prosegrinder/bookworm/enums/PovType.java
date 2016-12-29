package com.prosegrinder.bookworm.enums;

public enum PovType {
  FIRST("first"), SECOND("second"), THIRD("third"), UNKNOWN("unknown");

  private final String person;

  PovType(final String person) {
    this.person = person;
  }

  @Override
  public String toString() {
    return this.person;
  }

}
