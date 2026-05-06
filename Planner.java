import java.util.*;
import java.io.*; 
import java.nio.file.*;

public class Planner {
    public static void main(String[] args) {

        String filename = "Metrolink_times_linecolour(in).csv";
        Set<String> stationSet = new HashSet<>(); //For storing station names
        List<String> connections = new ArrayList<>(); //For storing connections
        Map<String, List<Connection>> graph = new HashMap<>(); //For storing the graph structure

        try{

            List<String> lines = Files.readAllLines(Paths.get(filename));
            String currentLine = ""; //Variable to remember which Metroline is currently being read

            for (String line : lines) {
                line =line.trim(); //line name + removing extra spaces
                
                if (line.isEmpty()) continue; //Skipping empty lines

                String[] parts = line.split(","); //Splitting each line into parts

                if (parts[0].trim().equals("From") && parts[1].trim().equals("To")){ //Removing the spaces from the header row
                    continue; //Skiping the header line
                }

                    String firstPart = parts[0].trim(); //Getting the line name
                    String secondPart = (parts.length > 1) ? parts[1].trim() : ""; //Checks if there is a part after the line/place name

                    boolean isLineNameRow = (secondPart.isEmpty() && !firstPart.equals("From")); //Checking if the row is a line name

                    if (isLineNameRow){
                        currentLine = firstPart;
                        System.out.println("Now reading line: "+ currentLine);
                        continue;
                    }

                    //Connection Row
                    if (parts.length >= 3){
                        //Setting up connection data from the parts array
                        String fromStation = parts[0].trim();
                        String toStation = parts[1].trim();
                        String travelTimeStr = parts[2].trim();

                        double travelTime = Double.parseDouble(travelTimeStr);

                        //Adding station names to the set
                        stationSet.add(fromStation);
                        stationSet.add(toStation);

                        //Storing the connection as a readable string
                        connections.add(fromStation + "->" + toStation + " (" +travelTime + "mins) on" + currentLine);
                        //Add connection in both directions
                        graph.computeIfAbsent(fromStation, k -> new ArrayList<>()).add(new Connection(toStation, travelTime, currentLine));
                    }

                }
            } catch (IOException e){
                System.out.println("Error reading file" + e.getMessage());
            }
        }
    }

    //Storing the information about a connection between two stations together
    class Connection {
        String station;
        double time;
        String line;

        Connection(String station, double time, String line) {

            this.station = station;
            this.time = time;
            this.line = line;
        }
    }