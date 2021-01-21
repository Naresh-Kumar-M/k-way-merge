package kwaysort;

import java.util.List;

public class KWaySort {

  private final FileSplitter fileSplitter;
  private final FileMerger fileMerger;
  private static final long MAX_SIZE = 2L;

  public KWaySort(String inputFileName, String outputFinalName, PlayerFieldEnum playerField) {
    this.fileSplitter = new FileSplitter(inputFileName, MAX_SIZE, playerField);
    this.fileMerger = new FileMerger(outputFinalName, playerField);
  }

  public void sort() throws Exception {
    List<String> fileNames = fileSplitter.splitToSortedFiles();
    fileMerger.mergeSortedFiles(fileNames);
  }

}
