# IDES
This Intrusion Detection Expert System (IDES) aims to identify suspicious behaviour in a user by comparing event data and raising any alarms if the user is acting out of usualness.

# Implementation
1. The events to be recorded are specified in Events.txt and they each contain a weighting that depends on how important an event is. Events are simply actions, like logging in or placing orders in a system.
2. Next, the program gathers a set of base data from BaseData.txt, where each line of data respresents a unique day. This base data contains the amount of times a user executes certain events in a system per day, and we use this to calculate averages and standard deviations for each event.
3. Now that the system is set up to record certain events, and has the base data for these events, it starts taking in daily data via TestEvents.txt. This file also represents the amount of times a user executes certain events in a system per day, and we compare this data to the base data. If the system finds that the user is producing results that are distanced from the base data enough to cross a specified threshold, then it raises alarms and determines that the user account may be comprimised by an attacker.

# Installation
1. Install the [JDK](https://www.oracle.com/java/technologies/downloads/)
2. Unzip repository contents and navigate your terminal to this folder
3. Run command: javac IDES.java
4. Run command: java IDES
5. Test execution can be found in TestExecution.JPG
