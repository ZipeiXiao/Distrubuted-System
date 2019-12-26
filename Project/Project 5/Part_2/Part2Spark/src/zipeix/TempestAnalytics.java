package zipeix;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Class TempestAnalytics will be running Spark within IntelliJ.
 * This is similar to what we did in Lab 9.
 */
public class TempestAnalytics {

    /**
     * configuration
     */
    static SparkConf sparkConf;
    /**
     * context
     */
    static JavaSparkContext sparkContext;
    /**
     * input file as RDD
     */
    static JavaRDD<String> inputFile;

    /**
     * Using the count method of the JavaRDD class,
     * display the number of lines in "The Tempest".
     * For the display, use System.out.println().
     */
    private static void task0() {
        // display the number of lines
        System.out.println(inputFile.count());
    }

    /**
     * Using the split method of the java String class and the flatMap method of the JavaRDD class,
     * use the count method of the JavaRDD class to display the number of words in The Tempest.
     * For the display, use System.out.println().
     */
    private static void task1() {
        // split the line by white space and flat RDD
        JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
        // count the number of words
        System.out.println(wordsFromFile.count());
    }

    /**
     * Using some of the work you did above and the JavaRDD distinct() and count() methods, display
     * the number of distinct words in The Tempest. For the display, use System.out.println().
     */
    private static void task2() {
        // split the line by white space and flat RDD
        JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
        // only save distinct words and count the number of words
        System.out.println(wordsFromFile.distinct().count());
    }

    /**
     * Using the JavaPairRDD class and the saveAsTextFile() method along with the JavaRDD class
     * and the mapToPair() method, show each word paired with the digit 1 in the output directory
     * named Project5/Part_2/TheTempestOutputDir1.
     */
    private static void task3() {
        // split the line by white space and flat RDD
        JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
        // each word paired with the digit 1
        JavaPairRDD countData = wordsFromFile.mapToPair(t -> new Tuple2(t, 1));
        // save the RDD into a text file
        countData.saveAsTextFile("Project5/Part_2/TheTempestOutputDir1");
    }

    /**
     * Using work from above and the JavaPairRDD from Task 3, create a new JavaPairRDD with the
     * reduceByKey() method. Save the RDD using the saveAsTextFile() method and place the result in
     * the output directory named Project5/Part_2/TheTempestOutputDir2. Here, we are not using
     * System.out.println().In my solution, one line of output is (magic,3).
     */
    private static void task4() {
        // split the line by white space and flat RDD
        JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
        // count the frequency of each word
        JavaPairRDD countData = wordsFromFile.mapToPair(t -> new Tuple2(t, 1)).reduceByKey((x, y) -> (int) x + (int) y);
        // save the RDD into a text file
        countData.saveAsTextFile("Project5/Part_2/TheTempestOutputDir2");
    }

    /**
     * Using work from above and the JavaRDD foreach() method, prompt the user for a string and
     * then perform a search on every line of the The Tempest. If any line of The Tempest
     * contains the String entered by the user then display the entire line. For the display,
     * use System.out.println().
     */
    private static void task5() {
        // prepare for user input
        Scanner sc = new Scanner(System.in);
        // instruct user to input a string for search
        System.out.println("enter a string:");
        // set the input string as the filter
        String input = sc.nextLine();
        // only keep the line which contains the input string and print out those lines
        inputFile.filter(line -> line.contains(input)).foreach(newline -> System.out.println(newline));
    }

    public static void main(String[] args) {
        // create a new config
        sparkConf = new SparkConf().setMaster("local").setAppName("JD Word Counter");
        // create a new spark context
        sparkContext = new JavaSparkContext(sparkConf);
        // load the file as RDD
        inputFile = sparkContext.textFile(args[0]);

        task0();
        task1();
        task2();
        task3();
        task4();
        task5();
    }
}
