/**
 * @author zipeix (Zipei Xiao)
 * @time 11/23/2019
 * Map-reduce program
 */
package org.myorg;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.*;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Within the /home/public directory, there is a file called P1V.txt.
 *
 * P1V.txt is a tab delimited text file with a individual criminal offense incidents
 * from January 1990 through December 1999 for serious violent crimes (FBI Part 1) in Pittsburgh.
 *
 * The first two columns (X,Y) represent State Plane (projected, rectilinear) coordinates (measured
 * in feet) specifying the location of the crime.
 * The third column is the time.
 * The fourth column is a street address.
 * The fifth column is the type of offense (aggravated assault, Robbery, Rape, Etc.)
 * The sixth column is the date.
 * The seventh column is the 2000 census tract.
 *
 * Write a MapReduce application that finds the total number of rapes and
 * robberies. If there were 100 rapes and 50 robberies then this program
 * would display 150 (100 + 50).
 */
public class Task6  extends Configured implements Tool {

    /**
     * This is a Mapper class that match each line with the type of Robbery or Rape with one
     */
    public static class Task6Mapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        // counter one
        private final static IntWritable one = new IntWritable(1);
        // the only key
        private Text total = new Text("Total");

        /**
         * This the map method that match each line with the type of Robbery or Rape with one
         * @param key empty
         * @param value file lines
         * @param context context to write
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // Get line from input file. This was passed in by Hadoop as value.
            // We have no use for the key (file offset) so we are ignoring it.
            String line = value.toString();
            // split it into columns by \t
            String[] columns = line.split("\t");

            // The fifth column is the type of offense (aggravated assault, Robbery, Rape, Etc.)
            String typeOfOffense = columns[4];

            // only write into context if the type is ROBBERY or RAPE
            if (typeOfOffense.equals("ROBBERY") || typeOfOffense.equals("RAPE")) {
                context.write(total, one);
            }
        }
    }

    /**
     * The reducer class for task6
     */
    public static class Task6Reducer extends Reducer<Text, IntWritable, NullWritable, IntWritable> {
        /**
         * The reduce method
         * @param key the ROBBERY or RAPE
         * @param values count
         * @param context context to write
         * @throws IOException
         * @throws InterruptedException
         */
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;

            // Robbery key
            Text robbery = new Text("Robbery");
            // Rape key
            Text rope = new Text("Rape");

            // sum up the count
            for(IntWritable value: values) {
                sum += value.get();
            }

            // write output to the file
            context.write(NullWritable.get(), new IntWritable(sum));
        }
    }

    /**
     * 1. Set the key/value class for the map output data.
     * 2. Set the key/value class for the job output data.
     * 3. Set the Mapper and Reducer class for the job.
     * 4. Set the number of Reducer tasks.
     * 5. Submit the job to the cluster and wait for it to finish, i.e.,
     *  {@code job.waitForCompletion(true);}
     * @param args
     * @return
     * @throws Exception
     */
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: Task6 <input path> <output path>");
            System.exit(-1);
        }

        // create a job
        Job job = new Job(getConf());
        job.setJarByClass(Task6.class);
        job.setJobName("Task6");

        // set the input and output path
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 1. Set the key/value class for the map output data.
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 3. Set the Mapper and Reducer class for the job.
        job.setMapperClass(Task6Mapper.class);
        job.setReducerClass(Task6Reducer.class);

        // set the input and the output format of the file
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        // 5. Submit the job to the cluster and wait for it to finish
        boolean success = job.waitForCompletion(true);
        return success ? 0: 1;
    }


    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        int result = ToolRunner.run(new Task6(), args);

        // prepare for user input
        Scanner scanner = null;
        try {
            // scan the file
            scanner = new Scanner(new File("/user/student125/output"), "latin1");
            // take lines from file
            while (scanner.hasNextLine()) {
                // read line by line
                String line = scanner.nextLine();
                // only shows non-empty line
                if (line != null && line.length() > 0) {
                    System.out.println(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find the file");
        } finally {
            // close the file finally
            if (scanner != null) {
                scanner.close();
            }
        }

        // stop map reduce job
        System.exit(result);
    }
}
