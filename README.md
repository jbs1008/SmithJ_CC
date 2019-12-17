Thank you for taking the time to view my project. In this file, you will see the following information.

- The purpose of this repository
- Steps for getting this application running
- An overview of my approach, design choices, and assumptions

# Purpose
This repository facilitates a Java application that reads in a CSV file, parses the information, and sends correct records into an SQLite database. Any incorrect records will be written to a separate CSV file. Additionally, statistics on the distribution of received/successful/unsuccessful record additions will be written to a log file. Ultimately, this application was made for my Mountain State Software Solutions application around the holiday season.

# Getting the Application Running
All of the dependencies are included in the project itself, and all file directories are made relative to the project itself, so it is as simple as downloading the project, opening the program with an IDE, and running it! 

# Overview
Overall, this project was quite simple. My approach was to use Maven (and some of its' dependencies) to create a single class program. I started the program off by instantiating/initializing anything I would need for later in the program, like a Connection object, misc. variables, objects to read and write to/from various files, etc. The actual algorithm for the reading, writing, and parsing, is no more than 25 lines of code. In those lines of code, I read in the lines until there are none left, put the incomplete records in an SCV file, and put the complete ones (adding escape characters where necessary) in an SQLite database. After that, I output the statistics to a log file. There wasn't much chance to deviate my approach besides for file structure and tools to use. However, since the scope of this program is very small, I chose to keep this in a single file, there was no need to overcomplicate it!

Assumptions that I made are the following:
- Kept the column names A through J, instead of giving them their own names.
- Since you had column headers in the original CSV file, I figured you would want them for the newly-created one.
- I figured we would want to keep all the single quotes in the database, so I appended escape characters to them so it wouldn't cause syntax errors.
- You will see that I have a fairly long if-statement testing whether a given record contains any empty elements. The efficiency of including an Apache Commons library and a loop would be the same, so I kept it as-is.
- In a normal database, I am very aware that I would not want to use a String array to populate the database (instead I would use an assortment of different variables), however I figured for the purpose of this coding challenge it was fine. If this was a real project, I would also create a Primary Key for the database, and most likely normalized it more. Additionally, encryption would most liekly be necessary.


# Notes:
- The program takes 5 seconds to complete without any database interaction, and 45 seconds with it (very specifications reliant, of course). I could not find any ways to improve the database interaction efficiency drastically, so If you come across a way please let me know! 
- A small error is still in this build, where 8 of the unsuccessful records don't actually get written to the CSV file. I have looked at a lot of portions of the CSV file and SQLite database to see what the error could be, but to no avail. 
- My time to work on this challenge is very limited due to the very busy holiday season, so I made this program as efficient and well-documented as I could given the time alloted. I hope you enjoy!


