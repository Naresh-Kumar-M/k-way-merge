package kwaysort;

import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class FileSplitter {

  private final String inputFileName;
  private final long maxSize;
  private final PlayerFieldEnum playerField;
  private static final String TEMP_FILE_NAME_PREFIX = "temp-file-";

  public FileSplitter(String inputFileName, long maxSize, PlayerFieldEnum playerField) {
    this.inputFileName = inputFileName;
    this.maxSize = maxSize;
    this.playerField = playerField;
  }

  public List<String> splitToSortedFiles() throws Exception {
    RandomAccessFile inputFile = new RandomAccessFile(inputFileName, "r");
    List<String> partitionedFiles = new ArrayList<>();
    Set<Player> lines = new TreeSet<>(playerField.comparator);
    long count = 1;
    do {
      String line = inputFile.readLine();
      if (null == line) {
        break;
      }
      Optional<Player> player = SerDes.toPlayer(line);
      if (player.isPresent()) {
        lines.add(player.get());
      }
      if (lines.size() == maxSize) {
        String tempFile = TEMP_FILE_NAME_PREFIX + count++;
        partitionedFiles.add(writeToTemporaryFile(tempFile, lines));
        lines = new TreeSet<>(playerField.comparator);
      }
    } while (true);
    if (!lines.isEmpty()) {
      partitionedFiles.add(writeToTemporaryFile(TEMP_FILE_NAME_PREFIX + count, lines));
    }
    inputFile.close();
    return partitionedFiles;
  }

  public String writeToTemporaryFile(String outputFileName, Set<Player> players) throws Exception {
    FileWriter writer = new FileWriter(outputFileName);
    for (Player player : players) {
      writer.write(SerDes.toMessage(player));
    }
    writer.close();
    return outputFileName;
  }

}
