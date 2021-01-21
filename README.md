# k Way Merge
## Introduction
The k-way merge code in project accepts large input files containing lines in below mentioned format and create a new file sorted accoring to the field mentioned
```sh
Name Age TShirtNumber
```
## Approach 
The large input file is read and split into multiple temporary files based on the mentioned size. The temporary file entries are sorted according to the criteria mentioned. The sorted temporary files are merged using priority queue. 

## Input location 
https://github.com/nareshm87/k-way-merge/blob/main/src/main/resources/input.txt
