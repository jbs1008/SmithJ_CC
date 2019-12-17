/*
Program Name - SmithJ_CC
Author - Joshua Smith
Date Last Revised - 12/17/2019
Purpose - A Java application that will consume a CSV file, parse the data, and insert valid
records into a SQLite database. Bad records will be put into another CSV file (people-bad),
and statistics will be written out to a log.
 */
package com.mycompany.smithj_cc;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.CSVReaderBuilder;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.opencsv.CSVWriter;
import java.io.Writer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CodingChallenge {

    //File directories that will be relatives to the programs file directory
    private static final String CSV_FILE_PATH = "./people.csv";
    private static final String GENERATED_CSV = "./people-bad.csv";
    private static final String GENERATED_LOG = "./statistics.log";

    public static void main(String[] args) throws CsvValidationException, ClassNotFoundException, SQLException {

        //The SQLite JDBC and instantiation of the connection class.
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;

        try {
            //Try to establish a connection to the codingChallenge database, erase the table if exists, and create a new table in its place (You could delete the rows instead, but likely more efficient to delete the table)
            connection = DriverManager.getConnection("jdbc:sqlite:codingChallenge.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate("DROP TABLE IF EXISTS people");
            statement.executeUpdate("CREATE TABLE people (A STRING, B STRING, C STRING, D STRING, E STRING, F STRING, G STRING, H STRING, I STRING, J STRING)");

            //Variables/arrays that are needed further in the program
            int received = 0;
            int correct = 0;
            int incorrect = 0;
            int sQuoteIndex = 0;
            char ch = '\'';
            String[] record;

            //Create a Reader and Writer object with the aforementioned relative file paths.
            Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
            Writer writer = Files.newBufferedWriter(Paths.get(GENERATED_CSV));

            //Create a new Logger
            Logger logger = Logger.getLogger("people");

            //New file handler object with aforementioned relative file path, and add it to the logger with simple formatting
            FileHandler fh;
            fh = new FileHandler(GENERATED_LOG);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            //Create a CSVWriter object 
            CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

            //This is what the column names for the CSV will be called, and they will be written to the file first.
            String[] headerRecord = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
            csvWriter.writeNext(headerRecord);

            //When reading the CSV file initially, it will not read/process the first (header) line of information.
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

            //Runs until the file runs out of content
            while ((record = csvReader.readNext()) != null) {

                //Increment the number of received records
                ++received;

                //If any element of the record is missing, increment the number of incorrect records by 1, check to see if there is anything after the record (in the file), and then write the incorrect record into the people-bad csv file.
                if (record[0].isEmpty() || record[1].isEmpty() || record[2].isEmpty() || record[3].isEmpty() || record[4].isEmpty() || record[5].isEmpty() || record[6].isEmpty() || record[7].isEmpty() || record[8].isEmpty() || record[9].isEmpty()) {
                    incorrect++;
                    if (csvReader.peek() != null) {
                        csvWriter.writeNext(new String[]{record[0], record[1], record[2], record[3], record[4], record[5], record[6], record[7], record[8], record[9]});
                    }

                    //If the record contains all the information, increment the number of correct records by 1    
                } else {
                    correct++;
                    //Check to see if any of the elements in a given record contain single quotes, and use a StringBuilder object to put an escape character before it.
                    for (int i = 0; i < 10; i++) {
                        if (record[i].contains("'")) {
                            sQuoteIndex = record[i].indexOf("'");
                            StringBuilder sb = new StringBuilder(record[i]);
                            sb.insert(sQuoteIndex, ch);
                            record[i] = sb.toString();
                        }

                    }
                    //Finally, insert the updated record to the database.
                    statement.executeUpdate("INSERT INTO people VALUES('" + record[0] + "', '" + record[1] + "', '" + record[2] + "', '" + record[3] + "', '" + record[4] + "', '" + record[5] + "', '" + record[6] + "', '" + record[7] + "', '" + record[8] + "', '" + record[9] + "')");

                }

            }

            //Log the received, successful, and unsuccessful record count to the statistics.log file
            logger.log(Level.INFO, "\n Number of records received: {0}", received - 1);
            logger.log(Level.INFO, "\n Number of successful records: {0}", correct);
            logger.log(Level.INFO, "\n Number of unsuccessful records: {0}", incorrect - 1);

            //Exception handling
        } catch (IOException e) {
        } finally {
            try {
                //Close the connection once the program has completed.
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }

    }

}
