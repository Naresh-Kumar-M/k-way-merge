package kwaysort;

import java.util.Comparator;

public enum PlayerFieldEnum {

  NAME(Comparator.comparing(Player::getName)), 
  AGE(Comparator.comparing(Player::getAge)),
  T_SHIRT_NUMBER(Comparator.comparing(Player::gettShirtNumber));

  public final Comparator<Player> comparator;

  private PlayerFieldEnum(Comparator<Player> comparing) {
    this.comparator = comparing;
  }
}
