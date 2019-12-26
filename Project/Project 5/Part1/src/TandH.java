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
 * This class reads the words.txt file and computes
 * the number of words in the file that contain the substring 'th'. That is,
 * we are counting words that contain the two lower case letters 't' and 'h' where
 * 't' is immediately followed by 'h'.
 */
public class TandH extends Configured implements Tool {
    /**
     * This is the mapper class for task2
     */
    public static class TandHMap extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text total = new Text("Total");

        /**
         * The Mapper function
         * @param key file lines
         * @param value
         * @param context context to write
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // read file line by line
            String line = value.toString();
            // split the line by space
            StringTokenizer tokenizer = new StringTokenizer(line);
            while(tokenizer.hasMoreTokens()) {
                // only writes the words that contain "th" into context
                if(containsTH(tokenizer.nextToken())) {
                    // map for count
                    context.write(total, one);
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
     * This is the Reducer class for task2
     */
    public static class TandHReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        /**
         * The reducer methods
         * @param key words
         * @param values 1
         * @param context
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
        job.setJarByClass(TandH.class);
        job.setJobName("TandH");

        // 1. Set the key/value class for the map output data.
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 3. Set the Mapper and Reducer class for the job.
        job.setMapperClass(TandHMap.class);
        job.setReducerClass(TandHReducer.class);

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
        int result = ToolRunner.run(new TandH(), args);
        System.exit(result);
    }
}
