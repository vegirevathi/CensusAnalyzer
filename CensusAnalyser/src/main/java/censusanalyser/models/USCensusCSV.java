package censusanalyser.models;

import com.opencsv.bean.CsvBindByName;

public class USCensusCSV {
    @CsvBindByName(column = "State Id", required = true)
    public String stateId;

    @CsvBindByName(column = "State", required = true)
    public String state;

    @CsvBindByName(column = "Population", required = true)
    public int population;

    @CsvBindByName(column = "Housing units", required = true)
    public int housingUnits;

    @CsvBindByName(column = "Total area", required = true)
    public Double totalArea;

    @CsvBindByName(column = "Water area", required = true)
    public Double waterArea;

    @CsvBindByName(column = "Land area", required = true)
    public Double landArea;

    @CsvBindByName(column = "Population Density", required = true)
    public Double populationDensity;

    @CsvBindByName(column = "Housing Density", required = true)
    public Double housingDensity;

    public USCensusCSV(String state, String stateCode, int population, double populationDensity, double totalArea) {
    }

    @Override
    public String toString() {
        return "usCensusCSV{" +
                "State Id='" + stateId + '\'' +
                ", State ='" + state + '\'' +
                ", Population='" + population + '\'' +
                ", Housing units='" + housingUnits + '\'' +
                ", Total area='" + totalArea + '\'' +
                ", Water area='" + waterArea + '\'' +
                ", Land Area='" + landArea + '\'' +
                ", Population Density='" + populationDensity + '\'' +
                ", Housing Density='" + housingDensity + '\'' +
                '}';
    }
}


