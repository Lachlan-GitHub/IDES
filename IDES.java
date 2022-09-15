import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;

public class IDES
{
    static class Event
    {
        String name;
        double average;
        double stdev;
        double weight;

        Event(String name, double weight)
        {
            this.name = name;
            this.weight = weight;
        }
    }

    static void input(ArrayList<Event> events) throws FileNotFoundException
    {
        //---Read Events.txt
		//First line contains the amount of events that will be monitored
		//Second line contains all the events and their weighting of improtance
		//Event objects are created with this data and are stored in the 'events' arraylist
        File file = new File("Events.txt");
        Scanner scanner = new Scanner(file);
		
		//The count of events to be monitored
        int eventCount = Integer.parseInt(scanner.nextLine());
		
		//Each event and their weight
        String[] eventData = scanner.nextLine().split(":");
        for (int i = 0; i < eventData.length; i += 2)
        {
            events.add(new Event(eventData[i], Double.parseDouble(eventData[i + 1])));
        }
		
		
		//---Read Base-Data.txt
		//Each line in this file represents a different day
		//Each number on the line represents how much the user executed the respective event on this given day
		//This base data will be used to test future data and determine if the user is behaving the same
        for (int i = 0; i < eventCount; i++)
        {
            file = new File("Base-Data.txt");
            scanner = new Scanner(file);
            ArrayList<Double> baseData = new ArrayList<>();
            double sum = 0;
            double days = 0;
			
			//Get all respective data from each day for a single event
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                String[] dataSplit = line.split(":");
                baseData.add(Double.parseDouble(dataSplit[i]));
                sum += Double.parseDouble(dataSplit[i]);
                days++;
            }
			
			//Set the average execution amount for the event
            double average = sum / days;
            events.get(i).average = average;

			//Set the stdev for the event
            double sum2 = 0;
            for (Double x : baseData)
            {
                x -= average;
                x *= x;
                sum2 += x;
            }
            events.get(i).stdev = Math.sqrt(sum2 / days);
        }
    }

    static void report(ArrayList<Event> events)
    {
		//Print out each event with its collected user data
        System.out.printf("%-30s %10s %10s %10s", "Event", "Average", "Stdev", "Weight");
        for (Event event : events)
        {
            System.out.printf("\n%-30s %10.2f %10.2f %10s", event.name, event.average, event.stdev, event.weight);
        }
    }

    static double calcThreshold(ArrayList<Event> d)
    {
		//A threshold should be set by system admins and can vary depending on how strict they want the system to be
		//The threshold respresents the max distance that the users actions can deviate from their usual measures before an alarm is raised
		//This program just sets the threshold as the sum of weights * 2
        double sum = 0;
        for (Event e : d)
        {
            sum += e.weight;
        }
        double threshold = 2 * sum;
        System.out.printf("\n\n%-15s %5.2f\n\n", "Threshold", threshold);
        return threshold;
    }

    static void test(ArrayList<Event> events, double threshold) throws FileNotFoundException
    {
		//---Read Test-Events.txt
		//Each line in this file represents a different day
		//Each number on the line represents how much the user executed the respective event on this given day
		//This test data will be tested aginst the base data to determine if the user is behaving the same
        File file = new File("Test-Events.txt");
        Scanner scanner = new Scanner(file);
        ArrayList<Double> testEvents = new ArrayList<>();
        int lineCount = 0;
		
		//Add all the numbers from Test-Events.txt into an ArrayList<Double>
        while (scanner.hasNextLine())
        {
            lineCount++;
            String[] split = scanner.nextLine().split(":");
            for (String num : split)
            {
                testEvents.add(Double.parseDouble(num));
            }
        }
		
		//Calculate the test data's distance from the base data
        for (int i = 0; i < lineCount * events.size(); i+= events.size())
        {
            for (int j = 0; j < events.size(); j++)
            {
                double x = (testEvents.get(i + j) - events.get(j).average) / events.get(j).stdev;
                if (x < 0)
                {
                    x *= -1;
                }
                testEvents.set(i + j, x * events.get(j).weight);
            }
        }
        scanner = new Scanner(file);
        int line = 1;
        for (int i = 0; i < lineCount * events.size(); i+= events.size())
        {
            String alarm = "";
            double sum = 0;
            for (int j = 0; j < events.size(); j++)
            {
                sum += testEvents.get(i + j);
            }

            if (sum <= threshold)
            {
                alarm = "No";
            }
            if (sum > threshold)
            {
                alarm = "Yes";
            }

            System.out.println("Line " + line + " -- " + scanner.nextLine() + " Distance: " + sum + " Alarm: " + alarm);
            line++;
        }
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        ArrayList<Event> events = new ArrayList<>();

        input(events);
        report(events);
        double threshold = calcThreshold(events);
        test(events, threshold);
    }
}
