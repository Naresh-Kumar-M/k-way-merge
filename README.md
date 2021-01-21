# k Way Merge
## Introduction
The k-way merge code in project accepts large input files containing lines in below mentioned format and create a new file sorted accoring to the field mentioned
```sh
Name Age TShirtNumber
```
## Approach 
The large input file is read and split into multiple temporary files based on the number of lines. Each temporary file entries are sorted according to the criteria mentioned. The sorted temporary files are merged using priority queue.
The queue at any point in time will hold the first entry that need to be processed from the file.(since the files are already sorted, the queue poll will always give the lowest of all the files). The approach basically follows bucket elimination approach in contrast to conventional k way merge in which a number of temporary files are created. This is to reduce disk access.

## Input location 
https://github.com/nareshm87/k-way-merge/blob/main/src/main/resources/input.txt
