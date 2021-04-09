package covidcases;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class InputHandler {
    private static final int MAX_COUNTRIES = 4;
    private static final String[] DEFAULT_COUNTRIES = {"south-africa", "canada"};
    private static final String EMPTY = "";
    private static final int PAST_WEEK = 7;

    private Scanner userInput = new Scanner(System.in);
    
    public void displayTitle(TextColour tc){
        tc.printTitle("---COVID Stats---\n# Author: Gagan Heer\n# April 6, 2021\n");
    }

    public LocalDate[] getDateInput(TextColour tc){
        String fromInput = null;
        String toInput = null;
        LocalDate fromDate = null;
        LocalDate toDate = null;

        try{
            tc.printTip("When entering dates please use format: yyyy-mm-dd\nex) 2021-01-15 for 2021 January 15");
            System.out.println("From Date:");
            fromInput = userInput.nextLine().trim();
            fromDate = LocalDate.parse(fromInput);
            System.out.println("To Date:");
            toInput = userInput.nextLine().trim();
            toDate = LocalDate.parse(toInput);
        }catch(DateTimeParseException dte){
            fromDate = LocalDate.now().minusDays(PAST_WEEK);
            toDate = LocalDate.now();
            tc.printWarning("INVALID date detected. Using default date.");
        }

        if(fromDate.compareTo(toDate) > 0){
            fromDate = LocalDate.now().minusDays(PAST_WEEK);
            toDate = LocalDate.now();
            tc.printWarning("Date mismatch detected. FROM date is more recent than TO date. Using default date.");
        }

        LocalDate[] dates = {fromDate, toDate};
        return dates;
    }

    public List<String> getCountryInput(TextColour tc){
        String countryInput = null;
        String[] countrySplit = null;
        List<String> countrySlugs = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();

        try{
            tc.printTip("\nWhen entering country list please use format: country1/country2/country3...\nUp to 4 countries can be listed");
            System.out.println("Country List:");
            countryInput = userInput.nextLine().trim();

            if(countryInput.equals(EMPTY)){
                countrySlugs.addAll(Arrays.asList(DEFAULT_COUNTRIES));
                tc.printWarning("No countries detected. Using default countries.\n");
            }else{
                countrySplit = countryInput.split("/");
                if(countrySplit.length > MAX_COUNTRIES){
                    tc.printWarning("More than " + MAX_COUNTRIES + " countries detected. Only " + MAX_COUNTRIES + " of them will be used.\n");
                }
            }
            userInput.close();

            if(countrySlugs.isEmpty()){
                var countryRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.covid19api.com/countries"))
                .GET()
                .build();

                var countryData = client.send(countryRequest, HttpResponse.BodyHandlers.ofString());
                Type listType = new TypeToken<ArrayList<CountryData>>(){}.getType();
                List<CountryData> countryList = new Gson().fromJson(countryData.body(), listType);
        
                for(CountryData temp : countryList){
                    for(int i = 0; i<countrySplit.length; i++){
                        if(temp.getCountry().equals(countrySplit[i])){
                            if(countrySlugs.size() < MAX_COUNTRIES){
                                countrySlugs.add(temp.getSlug());
                            }
                        }
                    }
                }

                if(countrySlugs.isEmpty()){
                    tc.printWarning("INVALID country names provided. Using default countries.");
                    countrySlugs.addAll(Arrays.asList(DEFAULT_COUNTRIES));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return countrySlugs;
    }

    
}
