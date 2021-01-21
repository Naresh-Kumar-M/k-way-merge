package kwaysort;

public class Player {
  private final String name;
  private final Integer age;
  private final Integer tShirtNumber;

  public Player(String name, Integer age, Integer tShirtNumber) {
    super();
    this.name = name;
    this.age = age;
    this.tShirtNumber = tShirtNumber;
  }

  public String getName() {
    return name;
  }

  public Integer getAge() {
    return age;
  }

  public Integer gettShirtNumber() {
    return tShirtNumber;
  }

}
