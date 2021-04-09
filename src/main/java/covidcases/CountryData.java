package covidcases;

public class CountryData {
    private String Country;
    private String Slug;
    private String ISO2;

    public CountryData(String country, String slug, String iso2){
        setCountry(country);
        setSlug(slug);
        setISO2(iso2);
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getSlug() {
        return Slug;
    }

    public void setSlug(String slug) {
        Slug = slug;
    }

    public String getISO2() {
        return ISO2;
    }

    public void setISO2(String iSO2) {
        ISO2 = iSO2;
    }

    
}
