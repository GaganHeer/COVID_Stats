package covidcases;

public class CovidData {
    
    private String ID;
    private String Country;
    private String CountryCode;
    private String Province;
    private String City;
    private String CityCode;
    private String Lat;
    private String Lon;
    private int Confirmed;
    private int Deaths;
    private int Recovered;
    private int Active;
    private String Date;
    private int NewCases;

    public CovidData(String id, String country, String countryCode, String province, String city, String cityCode, String lat, String lon, int confirmed, int deaths, int recovered, int active, String date) {
        setID(id);
        setCountry(country);
        setCountryCode(countryCode);
        setProvince(province);
        setCity(city);
        setCityCode(cityCode);
        setLat(lat);
        setLon(lon);
        setConfirmed(confirmed);
        setDeaths(deaths);
        setRecovered(recovered);
        setActive(active);
        setDate(date);
    }

    public CovidData(int confirmed, int deaths, int recovered, int active){
        setConfirmed(confirmed);
        setDeaths(deaths);
        setRecovered(recovered);
        setActive(active);
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLon() {
        return Lon;
    }

    public void setLon(String lon) {
        Lon = lon;
    }

    public int getConfirmed() {
        return Confirmed;
    }

    public void setConfirmed(int confirmed) {
        Confirmed = confirmed;
    }

    public int getDeaths() {
        return Deaths;
    }

    public void setDeaths(int deaths) {
        Deaths = deaths;
    }

    public int getRecovered() {
        return Recovered;
    }

    public void setRecovered(int recovered) {
        Recovered = recovered;
    }

    public int getActive() {
        return Active;
    }

    public void setActive(int active) {
        Active = active;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String cityCode) {
        CityCode = cityCode;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public int getNewCases() {
        return NewCases;
    }

    public void setNewCases(int newCases) {
        NewCases = newCases;
    }

    public void calcNewCases(int previousDay){
        setNewCases(getConfirmed() - previousDay);
    }

    
}
