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
import java.util.ArrayList;
import java.util.Scanner;

/**
 * In Task 8, the input file is named CrimeLatLonXYTabs.txt. It can be copied from the /home/public directory.
 * Its format is similar to P1V.txt but also includes the latitude and longitude of each crime.
 *
 * CrimeLatLonXYTabs.txt is a tab delimited text file with a individual criminal offense incidents
 * from January 1990 through December 1999 for serious violent crimes (FBI Part 1) in Pittsburgh.
 *
 * The first two columns (X,Y) represent State Plane (projected, rectilinear) coordinates (measured
 * in feet) specifying the location of the crime.
 * The third column is the time.
 * The fourth column is a street address.
 * The fifth column is the type of offense (aggravated assault, rape, Etc.)
 * The sixth column is the date.
 * The seventh column is the 2000 census tract.
 * The eight column specifies the latitude.
 * The ninth column specifies the longitude.
 *
 * These last two columns are used for viewing in GIS tools (such as Google Earth Pro).
 *
 * As in Task 7, modify your solution to Task 6 so that it finds all of the aggravated assault
 * crimes that occurred within 300 meters of 3803 Forbes Avenue in Oakland. This location has
 * the (X,Y) coordinates of (1354326.897,411447.7828). Use these coordinates and the Pythagorean
 * theorem to decide if a particular aggravated assault occurred within 300 meters of 3803 Forbes
 * Avenue. State plane coordinates are measured in feet. Your code is testing on meters.
 */
public class Task8  extends Configured implements Tool {

    /**
     * The mapper class for task8
     */
    public static class Task8Mapper extends Mapper<LongWritable, Text, Text, Text> {
        private Text total = new Text("Total");
        private Text point = new Text();

        /**
         * The map method for task8
         * @param key total
         * @param value one
         * @param context context to write
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // Get line from input file. This was passed in by Hadoop as value.
            // We have no use for the key (file offset) so we are ignoring it.
            String line = value.toString();
            // split line by \t
            String[] columns = line.split("\t");

            // initialize the double numbers
            double x = 0, y = 0, latitude = 0, longitude = 0;
            try {
                // The first two columns (X,Y) represent State Plane
                // (projected, rectilinear) coordinates (measured
                // in feet) specifying the location of the crime.
                x = Double.parseDouble(columns[0]);
                y = Double.parseDouble(columns[1]);
                // The eight column specifies the latitude.
                latitude = Double.parseDouble(columns[7]);
                // The ninth column specifies the longitude.
                longitude = Double.parseDouble(columns[8]);
            } catch (NumberFormatException e) {
                System.out.println("meet the head of the table");
            }

            // The fifth column is the type of offense (aggravated assault, Robbery, Rape, Etc.)
            String typeOfOffense = columns[4];

            // aggravated assault crimes that occurred within
            // 200 meters of 3803 Forbes Avenue in Oakland
            if (typeOfOffense.equals("AGGRAVATED ASSAULT")
                    && Pythagorean(x, y)) {
                // <!-- lon,lat[,alt] -->
                point.set(longitude + "," + latitude);
                // write the key and one into context
                context.write(total, point);
            }
        }

        /**
         * Check if occurred within
         * 300 meters of 3803 Forbes Avenue in Oakland
         * @param x coordinate X
         * @param y coordinate Y
         * @return true if occurred within 300 meters of 3803 Forbes Avenue in Oakland
         */
        private boolean Pythagorean(double x, double y) {
            // 3803 Forbes Avenue in Oakland: State plane coordinates are measured in feet.
            double coordinateX = 1354326.897;
            double coordinateY = 411447.7828;
            // distance in feet
            double distance = Math.sqrt((coordinateX - x) * (coordinateX - x) + (coordinateY - y) * (coordinateY - y));
            // 1 feet = 0.3048 meter
            if (distance * 0.3048 <= 300) {
                return true;
            }
            return false;
        }
    }

    /**
     * The reducer class for task8
     */
    public static class Task8Reducer extends Reducer<Text, Text, NullWritable, Text> {
        /**
         * The reduce method for task8
         * @param key total
         * @param values count
         * @param context context to write
         * @throws IOException
         * @throws InterruptedException
         */
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            // append all value to form the KML file
            String kml = KML(values);
            // the outpu is a KML file
            context.write(NullWritable.get(), new Text(kml));
        }

        /**
         * This is simple KML file sample:
         * <?xml version="1.0" encoding="UTF-8"?>
         * <kml xmlns="http://www.opengis.net/kml/2.2"> <Placemark>
         *  <name>Simple placemark</name>
         *  <description>Attached to the ground. Intelligently places itself at the height of the underlying terrain.</description>
         *  <Point>
         *  <coordinates>-122.0822035425683,37.42228990140251,0</coordinates>
         *  </Point>
         *  </Placemark> </kml>
         * @param values inputs
         * @return KML file
         */
        public String KML(Iterable<Text> values) {
            // append all values
            StringBuilder sb = new StringBuilder();
            // the KML file header
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                    "<Document>\n" +
                    "<name>KML Samples</name>\n" +
                    "<Folder>\n" +
                    "<name>Placemarks</name>\n" +
                    "<description>placemarks</description>\n");
            // add more placemarks
            for(Text value: values) {
                sb.append("<Placemark>\n<name>Simple placemark</name>\n" +
                        "<description>aggravated assault crimes that occurred within 300 meters of 3803 Forbes Avenue in Oakland.</description>\n" +
                        "<Point>\n" +
                        "<coordinates>");
                // <!-- lon,lat[,alt] -->
                sb.append(value.toString());
                sb.append(",0</coordinates>\n" +
                        "</Point>\n</Placemark>\n");
            }
            // end of a file
            sb.append("</Folder>\n" +
                    "</Document>\n</kml>");
            return sb.toString();
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
            System.err.println("Usage: Task8 <input path> <output path>");
            System.exit(-1);
        }

        // create a job
        Job job = new Job(getConf());
        job.setJarByClass(Task8.class);
        job.setJobName("Task8");

        // set the input and output path
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 1. Set the key/value class for the map output data.
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // 3. Set the Mapper and Reducer class for the job.
        job.setMapperClass(Task8Mapper.class);
        job.setReducerClass(Task8Reducer.class);

        // set the input and the output format of the file
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        // 5. Submit the job to the cluster and wait for it to finish
        boolean success = job.waitForCompletion(true);
        return success ? 0: 1;
    }

    /**
     * https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
     * @param files
     * @param folder
     */
    public static void listFilesForFolder (ArrayList<File> files, File folder) {
        try {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    listFilesForFolder(files, fileEntry);
                } else {
                    files.add(new File(fileEntry.getPath()));
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Cannot find the file");
        }
    }


    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        int result = ToolRunner.run(new Task8(), args);

        // prepare for user input
        Scanner scanner = null;
        try {
            // scan the file
            ArrayList<File> files = new ArrayList<>();
            final File folder = new File("/user/student125/output");
            listFilesForFolder(files, folder);
            // take lines from file
            for (File f : files) {
                scanner = new Scanner(f, "latin1");
                // read line by line
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    // only shows non-empty line
                    if (line != null && line.length() > 0) {
                        System.out.println(line);
                    }
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

        // stop the map reduce job
        System.exit(result);
    }
}
