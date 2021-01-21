package kwaysort;

public class AppMain {

  public static void main(String[] args) throws Exception {
    final String input = "c:/Temp/input.txt";
    final String output = "c:/Temp/output.txt";
    final PlayerFieldEnum criteria = PlayerFieldEnum.NAME;
    KWaySort kway = new KWaySort(input, output, criteria);
    kway.sort();
  }

}
