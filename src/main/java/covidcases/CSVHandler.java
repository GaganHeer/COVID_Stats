package covidcases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVWriter;

public class CSVHandler {

    private static final String DEFAULT_TIME = "T00:00:00Z";
    private TextColour tc = new TextColour();

    //TreeMap<Country, TreeMap<Date, COVID status numbers>>>
    public TreeMap<String, TreeMap<String, CovidData>> getCSVData(LocalDate[] dates, List<String> countrySlugs){
        TreeMap<String, TreeMap<String, CovidData>> csvData = new TreeMap<String, TreeMap<String, CovidData>>();
        LocalDate fromDate = dates[0];
        LocalDate toDate = dates[1];
        HttpClient client = HttpClient.newHttpClient();
        try{
            
            for(String tempSlug : countrySlugs){
                var dataRequest = HttpRequest.newBuilder()
                // Subtract one day because the COVID API returns data starting after the date given and we want to include the date given
                // Subtract another day in order to get previous days confirmed cases so that the first row of the 'New cases' column can be filled
                // The only data needed from going back one day is confirmed cases
                .uri(URI.create(("https://api.covid19api.com/live/country/" + tempSlug + "/status/confirmed/date/" + fromDate.minusDays(2).toString() + DEFAULT_TIME)))
                .GET()
                .build();
                
                var covidData = client.send(dataRequest, HttpResponse.BodyHandlers.ofString());
                Type listType = new TypeToken<ArrayList<CovidData>>(){}.getType();
                List<CovidData> covidDataList = new Gson().fromJson(covidData.body(), listType);
                int numSkips = 0;

                for(int i = 0; i<covidDataList.size(); i++){
                    LocalDate tempDate = LocalDate.parse(covidDataList.get(i).getDate().substring(0, 10));
                    // Always skip first the date because all we need from it is the confirmed cases data for the next iteration
                    // If the country reports cases by province instead of as a whole then the number of skips happening here will be equal to the number of provinces because all those cases fall on the same day
                    if((tempDate.compareTo(fromDate) < 0) || (tempDate.compareTo(toDate) > 0)){
                        numSkips++;
                        continue;
                    }

                    String country = covidDataList.get(i).getCountry();
                    String date = covidDataList.get(i).getDate();
                    CovidData data = new CovidData(covidDataList.get(i).getConfirmed(), covidDataList.get(i).getDeaths(), covidDataList.get(i).getRecovered(), covidDataList.get(i).getActive());
                    data.calcNewCases(covidDataList.get(i-numSkips).getConfirmed());

                    if(csvData.containsKey(country)){
                        if(csvData.get(country).containsKey(date)){
                            csvData.get(country).get(date).setConfirmed(data.getConfirmed() + csvData.get(country).get(date).getConfirmed());
                            csvData.get(country).get(date).setDeaths(data.getDeaths() + csvData.get(country).get(date).getDeaths());
                            csvData.get(country).get(date).setRecovered(data.getRecovered() + csvData.get(country).get(date).getRecovered());
                            csvData.get(country).get(date).setActive(data.getActive() + csvData.get(country).get(date).getActive());
                            csvData.get(country).get(date).setNewCases(data.getNewCases() + csvData.get(country).get(date).getNewCases());
                        }else{
                            csvData.get(country).put(date, data);
                        }
                    }else{
                        TreeMap<String, CovidData> tempMap = new TreeMap<>();
                        tempMap.put(date, data);
                        csvData.put(country, tempMap);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return csvData;
    }

    public void writeCSVData(TreeMap<String, TreeMap<String, CovidData>> csvData){
        try {
            File file = new File("data.csv");
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] headers = {"Country", "Date", "Confirmed", "Deaths", "Recovered", "Active", "New Cases"};
            writer.writeNext(headers, false);
    
            for (Map.Entry<String,TreeMap<String, CovidData>> countryEntry : csvData.entrySet()){
                for(Map.Entry<String, CovidData> dateEntry : countryEntry.getValue().entrySet()){
                    String[] nextLine = {
                        countryEntry.getKey(),
                        dateEntry.getKey(),
                        String.valueOf(dateEntry.getValue().getConfirmed()),
                        String.valueOf(dateEntry.getValue().getDeaths()),
                        String.valueOf(dateEntry.getValue().getRecovered()),
                        String.valueOf(dateEntry.getValue().getActive()),
                        String.valueOf(dateEntry.getValue().getNewCases())
                    };
                    writer.writeNext(nextLine, false);
                }
            }
            tc.printTip("Data file generated");
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeCSVSummary(TreeMap<String, TreeMap<String, CovidData>> csvData){
        try{
            File file = new File("summary.csv");
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] headers = {"Country", "Max Confirmed", "Min Confirmed", "Average Confirmed"};
            writer.writeNext(headers, false);

            for (Map.Entry<String,TreeMap<String, CovidData>> countryEntry : csvData.entrySet()){
                int maxCases = 0;
                int minCases = 0;
                int numCases = 0;
                int totalCases = 0;
                for(Map.Entry<String, CovidData> dateEntry : countryEntry.getValue().entrySet()){
                    int confirmed = dateEntry.getValue().getConfirmed();
                    totalCases += confirmed;
                    if(confirmed > maxCases || numCases == 0){
                        maxCases = confirmed;
                    }
                    if(confirmed < minCases || numCases == 0){
                        minCases = confirmed;
                    }
                    numCases++;
                }

                String[] nextLine = {
                    countryEntry.getKey(),
                    String.valueOf(maxCases),
                    String.valueOf(minCases),
                    String.valueOf(totalCases/numCases)
                };

                writer.writeNext(nextLine, false);
            }
            tc.printTip("Summary file generated");
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
