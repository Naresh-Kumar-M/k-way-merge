package kwaysort;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import org.apache.commons.io.FileUtils;

public class FileMerger {

  private static class Pair {
    private final String fileName;
    private final Player player;

    Pair(String fileName, Player player) {
      this.fileName = fileName;
      this.player = player;
    }
  }

  private final String outputFinalName;
  public final Comparator<Pair> comparator;

  public FileMerger(String outputFinalName, PlayerFieldEnum playerField) {
    this.outputFinalName = outputFinalName;
    this.comparator = new Comparator<FileMerger.Pair>() {

      @Override
      public int compare(Pair p1, Pair p2) {
        switch (playerField) {
          case NAME:
            return p1.player.getName().compareTo(p2.player.getName());
          case AGE:
            return p1.player.getAge().compareTo(p2.player.getAge());
          case T_SHIRT_NUMBER:
            return p1.player.gettShirtNumber().compareTo(p2.player.gettShirtNumber());
          default:
            return 0;
        }
      }
    };
  }

  public void mergeSortedFiles(List<String> fileNames) throws Exception {
    Map<String, RandomAccessFile> fileToHandleMap = getFileHandlesForTemporaryFiles(fileNames);
    Queue<Pair> queue = new PriorityQueue<>(comparator);
    queue.addAll(getFirstEntriesFromAllTemporaryFile(fileToHandleMap));
    while (!queue.isEmpty()) {
      Pair pair = queue.poll();
      appendToFile(outputFinalName, SerDes.toMessage(pair.player));
      Optional<Pair> playerPair =
          readLineFromFileHandle(pair.fileName, fileToHandleMap.get(pair.fileName));
      if (playerPair.isPresent()) {
        queue.add(playerPair.get());
      }
    }
    queue.addAll(getLeftOverEntriesFromAllTemporaryFile(fileToHandleMap));
    while (!queue.isEmpty()) {
      Pair pair = queue.poll();
      appendToFile(outputFinalName, SerDes.toMessage(pair.player));
    }    
    closeAndDeleteTemporaryFiles(fileToHandleMap);
  }

  private void closeAndDeleteTemporaryFiles(Map<String, RandomAccessFile> fileToHandleMap) throws IOException {
    for (String name : fileToHandleMap.keySet()) {
      fileToHandleMap.get(name).close();
      FileUtils.forceDelete(new File(name));
    }
  }

  private List<Pair> getLeftOverEntriesFromAllTemporaryFile(
      Map<String, RandomAccessFile> fileToHandleMap) throws Exception {
    List<Pair> leftOverEntries = new ArrayList<>();
    for (String file : fileToHandleMap.keySet()) {
      leftOverEntries.addAll(readAllLinesFromFileHandle(file, fileToHandleMap.get(file)));
    }
    return leftOverEntries;
  }

  private List<Pair> getFirstEntriesFromAllTemporaryFile(
      Map<String, RandomAccessFile> fileToHandleMap) throws Exception {
    List<Pair> firstEntries = new ArrayList<>();
    for (String file : fileToHandleMap.keySet()) {
      Optional<Pair> playerPair = readLineFromFileHandle(file, fileToHandleMap.get(file));
      if (playerPair.isPresent()) {
        firstEntries.add(playerPair.get());
      }
    }
    return firstEntries;
  }

  private List<Pair> readAllLinesFromFileHandle(String file, RandomAccessFile randomAccessFile)
      throws Exception {
    List<Pair> pairs = new ArrayList<>();
    do {
      String line = randomAccessFile.readLine();
      if (null == line) {
        break;
      }
      Optional<Player> player = SerDes.toPlayer(line);
      if (player.isPresent()) {
        pairs.add(new Pair(file, player.get()));
      }
    } while (true);
    return pairs;
  }

  private Map<String, RandomAccessFile> getFileHandlesForTemporaryFiles(List<String> fileNames)
      throws FileNotFoundException {
    Map<String, RandomAccessFile> fileNameToFileHandles = new HashMap<String, RandomAccessFile>();
    for (String fileName : fileNames) {
      fileNameToFileHandles.put(fileName, new RandomAccessFile(fileName, "r"));
    }
    return fileNameToFileHandles;
  }

  private Optional<Pair> readLineFromFileHandle(String file, RandomAccessFile randomAccessFile)
      throws Exception {
    String line = randomAccessFile.readLine();
    if (null == line) {
      return Optional.empty();
    } else {
      return Optional.ofNullable(new Pair(file, SerDes.toPlayer(line).get()));
    }
  }

  private void appendToFile(String fileName, String text) throws Exception {
    File f = new File(fileName);
    long fileLength = f.length();
    RandomAccessFile raf = new RandomAccessFile(f, "rw");
    raf.seek(fileLength);
    raf.writeBytes(text);
    raf.close();
  }

}
