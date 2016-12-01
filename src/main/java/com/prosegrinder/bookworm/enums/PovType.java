package com.prosegrinder.bookworm.enums;

public enum PovType {
  FIRST(1), SECOND(2), THIRD(3), UNKNOWN(0);

  private final int person;

  PovType(final int person) {
    this.person = person;
  }

  private int person() {
    return this.person;
  }

}
