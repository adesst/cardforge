CardForge
=========

Project Summary
=========
Back in the summer of 2010, I began to work on an open source Magic the Gathering simulator project called CardForge. I am a Magic addict, I’ve been playing it since middle school back in 2002. My first set that I bought was Judgement.
Anyways, I thought why not develop something I have a passion in? So I looked up the project, which was hosted on Google Code at the time, joined the forums, and became an active contributer, with the committer name xitongzou.

Technologies Used
=========
CardForge is entirely Java-based and uses Maven for building and dependency management. The simulator currently cannot simulate multiplayer nor 100% optimal AI actions due to the interactions of MTG’s 10000+ cards, but it has a deck editor and a decent single player practice mode.

Responsibilities/Roles
=========
I implemented about 60 of my favorite cards, including Bloodfire Colossus and Molten Hydra.

Project Details
=========
Since the project is open source, I can divulge some of the implementation of the cards. The Card.java file is very big, and contains all the different abilities of each creature.

-Tong Zou

(Addition - adesst)

Install
=========

Linux/MacOS:

Clone this repository
$ git clone https://github.com/adesst/cardforge

Make a dependency folder
$ mkdir dependency
$ cd dependency

Init and install the jyield lib
$ git clone https://github.com/adesst/jyield.git
$ cd jyield/jyield
$ mvn compile
$ mvn install:install-file -Dfile=lib/java-yield-1.0-SNAPSHOT.jar -DgroupId=com.google.code.jyield -DartifactId=jyield -Dversion=1.0-SNAPSHOT -Dpackaging=jar

Compile cardforge, make sure you are in the cardforge/ dir
$ pwd
%yourdir%/cardforge
$ mvn compile assemble:single

Execute and run the jar file to start the game
$ java -jar ./target/forge-1.1.1-SNAPSHOT-jar-with-dependencies.jar



Windows:

Contributor wanted, to fill in this how-to compile, install in Win based OS
