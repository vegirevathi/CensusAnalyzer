package censusanalyser.service;

import censusanalyser.adapters.CensusAdapterFactory;
import censusanalyser.exception.CensusAnalyserException;
import censusanalyser.models.censusDAO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

public class CensusAnalyser {

    private Country country;

    List<censusDAO> censusList = new ArrayList<>();
    public enum Country { INDIA, US }

    public CensusAnalyser(Country country) {
        this.country = country;
    }

    public int loadCensusData(Country country, String... csvFilePath) throws CensusAnalyserException {
        censusList = CensusAdapterFactory.getCensusData(country, csvFilePath);
        return censusList.size();
    }

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw  new CensusAnalyserException("No Census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusComparator = Comparator.comparing(census -> census.state);
        ArrayList censusDTOS= censusList.stream().
                                sorted(censusComparator).
                                map(censusDAO -> censusDAO.getCensusDTO(country)).
                                collect(toCollection(ArrayList::new));
        String sortedStateCensusJson = new Gson().toJson(censusDTOS);
        return sortedStateCensusJson;
    }

    public String getStateWiseSortedStateCodeData() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw  new CensusAnalyserException("No Census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusComparator = Comparator.comparing(census -> census.stateCode);
        ArrayList censusDTOS= censusList.stream().
                                sorted(censusComparator).
                                map(censusDAO -> censusDAO.getCensusDTO(country)).
                                collect(toCollection(ArrayList::new));
        String sortedStateCodeCensusJson = new Gson().toJson(censusDTOS);
        return sortedStateCodeCensusJson;
    }

    public String getStateWiseSortedCensusDataOnPopulation() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusCSVComparator = Comparator.comparing(census -> census.population);
        ArrayList censusDTOS= censusList.stream().
                                sorted(censusCSVComparator.reversed()).
                                map(censusDAO -> censusDAO.getCensusDTO(country)).
                                collect(toCollection(ArrayList::new));
        String sortedPopulation = new Gson().toJson(censusDTOS);
        return sortedPopulation;
    }

    public String getStateWiseSortedCensusDataOnPopulationDensity() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw  new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusCSVComparator = Comparator.comparing(census -> census.populationDensity);
        ArrayList censusDTOS= censusList.stream().
                                sorted(censusCSVComparator.reversed()).
                                map(censusDAO -> censusDAO.getCensusDTO(country)).
                                collect(toCollection(ArrayList::new));
        String sortedPopulationDensity = new Gson().toJson(censusDTOS);
        return sortedPopulationDensity;
    }

    public String getStateWiseSortedCensusDataOnArea() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw  new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusCSVComparator = Comparator.comparing(census -> census.totalArea);
        ArrayList censusDTOS= censusList.stream().
                                sorted(censusCSVComparator.reversed()).
                                map(censusDAO -> censusDAO.getCensusDTO(country)).
                                collect(toCollection(ArrayList::new));
        String sortedAreaInSqKm = new Gson().toJson(censusDTOS);
        return sortedAreaInSqKm;
    }

    public String getUSStateWiseSortedCensusDataOnPopulation() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No census data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusComparator = Comparator.comparing(census -> census.population);
        ArrayList censusDTOS= censusList.stream().
                                sorted(censusComparator.reversed()).
                                map(censusDAO -> censusDAO.getCensusDTO(country)).
                                collect(toCollection(ArrayList::new));
        String sortedPopulationCensusJson = new Gson().toJson(censusDTOS);
        return sortedPopulationCensusJson;
    }

    public String getUSStateWiseSortedCensusDataOnPopulationDensity() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw new CensusAnalyserException("No census data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusCSVComparator = Comparator.comparing(census -> census.populationDensity);
        ArrayList censusDTOS= censusList.stream().
                                sorted(censusCSVComparator.reversed()).
                                map(censusDAO -> censusDAO.getCensusDTO(country)).
                                collect(toCollection(ArrayList::new));
        String sortedPopulationDensity = new Gson().toJson(censusDTOS);
        return sortedPopulationDensity;
    }

    public String getUSStateWiseSortedCensusDataOnTotalArea() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusCSVComparator = Comparator.comparing(census -> census.totalArea);
        ArrayList censusDTOS= censusList.stream().
                                sorted(censusCSVComparator).
                                map(censusDAO -> censusDAO.getCensusDTO(country)).
                                collect(toCollection(ArrayList::new));
        String sortedTotalArea = new Gson().toJson(censusDTOS);
        return sortedTotalArea;
    }

    public String mostPopulationDensityStateInIndiaAndUS() {
        Comparator<censusDAO> censusCSVComparator = Comparator.comparing(census -> census.populationDensity);
        ArrayList censusDTOS= censusList.stream().
                                sorted(censusCSVComparator).
                                map(censusDAO -> censusDAO.getCensusDTO(country)).
                                collect(toCollection(ArrayList::new));
        String sortedPopulationDensityUS = new Gson().toJson(censusDTOS);
        return sortedPopulationDensityUS;
    }

}
