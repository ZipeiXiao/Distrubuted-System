=================================================TASK0======================================================
hadoop dfs -copyFromLocal /home/public/words.txt /user/student125/input/words.txt
hadoop dfs -rm /user/student125/input/test
hadoop dfs -ls /user/student125/input

javac -classpath  /usr/local/hadoop/hadoop-core-1.2.1.jar:./Project5 -d Project5 WordCount.java

jar -cvf wordcount.jar -C  Project5/  .

hadoop dfs -rmr /user/student125/output

hadoop jar /home/student125/wordcount.jar org.myorg.WordCount  /home/public/words.txt /user/student125/output

hadoop dfs -cat /user/student125/output/part-r-00000

hadoop dfs -ls /user/student125/output

hadoop dfs -getmerge /user/student125/output ~/Project5/Part_1/Task0/Task0Output/

=================================================TASK1======================================================
cd Project5/Part_1
mkdir Task1
cd Task1
mkdir Task1Output
cd /home/student125
javac -classpath  /usr/local/hadoop/hadoop-core-1.2.1.jar:./Project5 -d Project5 TotalWords.java
jar -cvf total_words.jar -C  Project5/  .
hadoop dfs -rmr /user/student125/output
hadoop jar /home/student125/total_words.jar org.myorg.TotalWords  /home/public/words.txt /user/student125/output
hadoop dfs -ls /user/student125/output
hadoop dfs -getmerge /user/student125/output ~/Project5/Part_1/Task1/Task1Output
vi ~/Project5/Part_1/Task1/Task1Output/output

=================================================TASK2======================================================
cd Project5/Part_1
mkdir Task2
cd /home/student125
javac -classpath  /usr/local/hadoop/hadoop-core-1.2.1.jar:./Project5 -d Project5 TandH.java
jar -cvf tandh.jar -C  Project5/  .

hadoop dfs -rmr /user/student125/output
hadoop jar /home/student125/tandh.jar org.myorg.TandH  /home/public/words.txt /user/student125/output
hadoop dfs -ls /user/student125/output
hadoop dfs -getmerge /user/student125/output ~/Project5/Part_1/Task2/Task2Output

hadoop dfs -cat /user/student125/output/part-r-00000
vi ~/Project5/Part_1/Task2/Task2Output

=================================================TASK3======================================================
cd Project5/Part_1
mkdir Task3
cd /home/student125
javac -classpath  /usr/local/hadoop/hadoop-core-1.2.1.jar:./Project5 -d Project5 THandNotTH.java
jar -cvf thandnotth.jar -C  Project5/  .
hadoop dfs -rmr /user/student125/output
hadoop jar /home/student125/thandnotth.jar org.myorg.THandNotTH  /home/public/words.txt /user/student125/output
hadoop dfs -ls /user/student125/output
hadoop dfs -getmerge /user/student125/output ~/Project5/Part_1/Task3/Task3Output

hadoop dfs -cat /user/student125/output/part-r-00000
vi ~/Project5/Part_1/Task3/Task3Output

=================================================TASK4======================================================
cd Project5/Part_1
mkdir Task4
cd /home/student125
hadoop dfs -copyFromLocal /home/student125/input/combinedYears.txt /user/student125/input/combinedYears.txt
hadoop dfs -cat /user/student125/input/testFile
mkdir task4_classes
javac -classpath  /usr/local/hadoop/hadoop-core-1.2.1.jar:./Project5 -d Project5 MaxTemperatureMapper.java
javac -classpath  /usr/local/hadoop/hadoop-core-1.2.1.jar:./Project5 -d Project5 MaxTemperatureReducer.java
javac -classpath  /usr/local/hadoop/hadoop-core-1.2.1.jar:./Project5 -d Project5 MaxTemperature.java
jar -cvf maxtemperature.jar -C  Task4/  .

hadoop dfs -copyFromLocal /home/public/combinedYears.txt /user/student125/input/combinedYears.txt
hadoop dfs -rmr /user/student125/output
hadoop jar /home/student125/maxtemperature.jar edu.cmu.andrew.mm6.MaxTemperature  /user/student125/input/combinedYears.txt /user/student125/output
hadoop dfs -ls /user/student125/output
hadoop dfs -getmerge /user/student125/output ~/Project5/Part_1/Task4/Task4Output
vi ~/Project5/Part_1/Task4/Task4Output

=================================================TASK5========================================================
cd Project5/Part_1
mkdir Task5
cd /home/student125
javac -classpath  /usr/local/hadoop/hadoop-core-1.2.1.jar:./Project5 -d Project5 MinTemperatureMapper.java
javac -classpath  /usr/local/hadoop/hadoop-core-1.2.1.jar:./Project5 -d Project5 MinTemperatureReducer.java
javac -classpath  /usr/local/hadoop/hadoop-core-1.2.1.jar:./Project5 -d Project5 MinTemperature.java
cd Project5
mkdir Task5_classes
jar -cvf mintemperature.jar -C  Task5_classes/  .

hadoop dfs -rmr /user/student125/output
hadoop jar /home/student125/mintemperature.jar MinTemperature  /user/student125/input/combinedYears.txt /user/student125/output
hadoop dfs -ls /user/student125/output
hadoop dfs -getmerge /user/student125/output ~/Project5/Part_1/Task5/Task5Output
vi ~/Project5/Part_1/Task5/Task5Output

=================================================TASK6=========================================================
cd Project5/Part_1
mkdir Task6
cd /home/student125
hadoop dfs -copyFromLocal /home/student125/input/P1V.txt /user/student125/input/P1V.txt
javac -classpath  /usr/local/hadoop/hadoop-core-1.2.1.jar:./Project5 -d Project5 Task6.java
jar -cvf rapesplusrobberies.jar -C  Project5/  .
hadoop dfs -rmr /user/student125/output
hadoop jar /home/student125/rapesplusrobberies.jar org.myorg.Task6  /user/student125/input/P1V.txt /user/student125/output
hadoop dfs -ls /user/student125/output
hadoop dfs -getmerge /user/student125/output ~/Project5/Part_1/Task6/Task6Output
vi ~/Project5/Part_1/Task6/Task6Output

=================================================TASK7=========================================================
cd Project5/Part_1
mkdir Task7
cd /home/student125
javac -classpath  /usr/local/hadoop/hadoop-core-1.2.1.jar:./Project5 -d Project5 Task7.java
jar -cvf oaklandcrimestats.jar -C  Project5/  .
hadoop dfs -rmr /user/student125/output
hadoop jar /home/student125/oaklandcrimestats.jar org.myorg.Task7  /user/student125/input/P1V.txt /user/student125/output
hadoop dfs -ls /user/student125/output
hadoop dfs -getmerge /user/student125/output ~/Project5/Part_1/Task7/Task7Output
vi ~/Project5/Part_1/Task7/Task7Output

=================================================TASK8=========================================================
cd Project5/Part_1
mkdir Task8
cd /home/student125
hadoop dfs -copyFromLocal /home/public/CrimeLatLonXYTabs.txt /user/student125/input/CrimeLatLonXYTabs.txt
javac -classpath  /usr/local/hadoop/hadoop-core-1.2.1.jar:./Project5 -d Project5 Task8.java
jar -cvf oaklandcrimestatskml.jar -C  Project5/  .
hadoop dfs -rmr /user/student125/output
hadoop jar /home/student125/oaklandcrimestatskml.jar org.myorg.Task8  /user/student125/input/CrimeLatLonXYTabs.txt /user/student125/output
hadoop dfs -ls /user/student125/output
hadoop dfs -getmerge /user/student125/output ~/Project5/Part_1/Task8/Task8Output
vi ~/Project5/Part_1/Task8/Task8Output

=================================================PART2=========================================================
cd Project5
mkdir Part_2
cd Part_2
mkdir Project5Spark
cd /home/student125

zip -r zipeix.zip Part_1