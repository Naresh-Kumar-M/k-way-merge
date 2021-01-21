package kwaysort;

import java.util.Optional;
import java.util.StringJoiner;
import org.apache.commons.lang3.StringUtils;

public class SerDes {

  public static String toMessage(Player player) {
    return player.getName() + " " + player.getAge() + " " + player.gettShirtNumber()
        + System.lineSeparator();
  }

  // S R Ten 40 10
  public static Optional<Player> toPlayer(String line) {
    if (null == line || line.isEmpty()) {
      return Optional.empty();
    }
    boolean flag = false;
    StringJoiner name = new StringJoiner(" ");
    Integer age = null;
    Integer tShirtNumber = null;
    String[] fragments = line.split("\\s+");
    for (String text : fragments) {
      if (!StringUtils.isNumeric(text)) {
        name.add(text);
      } else if (!flag) {
        age = Integer.parseInt(text);
        flag = true;
      } else {
        tShirtNumber = Integer.parseInt(text);
      }
    }
    if (null == age || null == tShirtNumber || name.toString().toString().isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(new Player(name.toString(), age, tShirtNumber));
  }
}
