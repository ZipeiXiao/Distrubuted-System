package zipeix;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Scanner;

public class TempestAnalytics {

    static SparkConf sparkConf;
    static JavaSparkContext sparkContext;
    static JavaRDD<String> inputFile;

    /**
     * Using the count method of the JavaRDD class,
     * display the number of lines in "The Tempest".
     * For the display, use System.out.println().
     */
    private static void task0() {
        System.out.println(inputFile.count());
    }

    /**
     * Using the split method of the java String class and the flatMap method of the JavaRDD class,
     * use the count method of the JavaRDD class to display the number of words in The Tempest.
     * For the display, use System.out.println().
     */
    private static void task1() {

        JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
        System.out.println(wordsFromFile.count());
    }

    /**
     * Using some of the work you did above and the JavaRDD distinct() and count() methods, display
     * the number of distinct words in The Tempest. For the display, use System.out.println().
     */
    private static void task2() {
        JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
        System.out.println(wordsFromFile.distinct().count());
    }

    /**
     * Using the JavaPairRDD class and the saveAsTextFile() method along with the JavaRDD class
     * and the mapToPair() method, show each word paired with the digit 1 in the output directory
     * named Project5/Part_2/TheTempestOutputDir1. You may re-use RDD's from the above tasks if
     * appropriate. Here, we are not using System.out.println().
     */
    private static void task3() {
        JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
        JavaPairRDD countData = wordsFromFile.mapToPair(t -> new Tuple2(t, 1));//.reduceByKey((x, y) -> (int) x + (int) y);
        countData.saveAsTextFile("Project5/Part_2/TheTempestOutputDir1");
    }

    /**
     * Using work from above and the JavaPairRDD from Task 3, create a new JavaPairRDD with the
     * reduceByKey() method. Save the RDD using the saveAsTextFile() method and place the result in
     * the output directory named Project5/Part_2/TheTempestOutputDir2. Here, we are not using
     * System.out.println().In my solution, one line of output is (magic,3).
     */
    private static void task4() {
        JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
        JavaPairRDD countData = wordsFromFile.mapToPair(t -> new Tuple2(t, 1)).reduceByKey((x, y) -> (int) x + (int) y);
        countData.saveAsTextFile("Project5/Part_2/TheTempestOutputDir2");
    }

    /**
     * Using work from above and the JavaRDD foreach() method, prompt the user for a string and
     * then perform a search on every line of the The Tempest. If any line of The Tempest
     * contains the String entered by the user then display the entire line. For the display,
     * use System.out.println().
     */
    private static void task5() {
        Scanner sc = new Scanner(System.in);
        System.out.println("enter a string:");
        String input = sc.nextLine();
        inputFile.filter(line -> line.contains(input)).foreach(newline -> System.out.println(newline));
    }

    public static void main(String[] args) {
        sparkConf = new SparkConf().setMaster("local").setAppName("JD Word Counter");
        sparkContext = new JavaSparkContext(sparkConf);
        inputFile = sparkContext.textFile(args[0]);

        if (args.length == 0) {
            System.out.println("No files provided.");
            System.exit(0);
        }

        task0();
        task1();
        task2();
        task3();
        task4();
        task5();
    }
}
