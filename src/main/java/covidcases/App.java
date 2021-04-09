package covidcases;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

public class App{
    public static void main(String[] args){
        
        TextColour tc = new TextColour();
        InputHandler inputHandler = new InputHandler();
        CSVHandler csvHandler = new CSVHandler();
        inputHandler.displayTitle(tc);
        LocalDate[] dates = inputHandler.getDateInput(tc);
        List<String> countrySlugs = inputHandler.getCountryInput(tc);
        TreeMap<String, TreeMap<String, CovidData>> csvData = csvHandler.getCSVData(dates, countrySlugs);
        csvHandler.writeCSVData(csvData);
        csvHandler.writeCSVSummary(csvData);
    }
}
