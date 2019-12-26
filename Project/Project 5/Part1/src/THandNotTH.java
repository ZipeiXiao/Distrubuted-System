/**
 * @author zipeix (Zipei Xiao)
 * @time 11/23/2019
 * Map-reduce program
 */
package org.myorg;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.*;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * This class display the number of words that
 * contain 'th'. It also displays the number of words that do
 * not contain 'th'.
 */
public class THandNotTH extends Configured implements Tool {
    /**
     * The Mapper class for task3
     */
    public static class THandNotTHMap extends Mapper<LongWritable, Text, Text, IntWritable> {
        // int one
        private final static IntWritable one = new IntWritable(1);
        // key for words that Contains TH
        private Text containsTHtotal = new Text("Contains TH Total");
        // key for words that do not Contains TH
        private Text notContainsTHtotal = new Text("Not Contains TH Total");

        /**
         * The map method for mapping two groups of words each word with one
         * @param key Contains TH Total or Not Contains TH Total
         * @param value count
         * @param context context to write
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // read the file line bu line
            String line = value.toString();
            // split the line by space
            StringTokenizer tokenizer = new StringTokenizer(line);
            while(tokenizer.hasMoreTokens()) {
                // writes the words that do not contain "th" into context
                if(!containsTH(tokenizer.nextToken())) {
                    context.write(notContainsTHtotal, one);
                } else {
                    // writes the words that contain "th" into context
                    context.write(containsTHtotal, one);
                }
            }
        }

        /**
         * Simple private helper method to validate a word.
         * @param text text to check
         * @return true if valid, false if not
         */
        private boolean containsTH(String text) {
            return text.matches(".*(th)+.*");
        }
    }

    /**
     * The reducer class for task3
     */
    public static class THandNotTHReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        /**
         * The reducer methods
         * @param key Contains TH Total or Not Contains TH Total
         * @param values count
         * @param context context to write
         * @throws IOException
         * @throws InterruptedException
         */
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            // sum up the count
            for(IntWritable value: values) {
                sum += value.get();
            }
            //write the output to file
            context.write(key, new IntWritable(sum));
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

        // create a job
        Job job = new Job(getConf());
        job.setJarByClass(THandNotTH.class);
        job.setJobName("THandNotTH");

        // 1. Set the key/value class for the map output data.
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 3. Set the Mapper and Reducer class for the job.
        job.setMapperClass(THandNotTHMap.class);
        job.setReducerClass(THandNotTHReducer.class);

        // 2. Set the key/value class for the job output data.
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        // set the input and output path
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 5. Submit the job to the cluster and wait for it to finish
        boolean success = job.waitForCompletion(true);
        return success ? 0: 1;
    }


    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        int result = ToolRunner.run(new THandNotTH(), args);
        System.exit(result);
    }
}
