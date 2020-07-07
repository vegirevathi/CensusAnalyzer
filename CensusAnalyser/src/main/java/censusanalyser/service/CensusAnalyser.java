package censusanalyser.service;

import censusanalyser.exception.CensusAnalyserException;
import censusanalyser.models.censusDAO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CensusAnalyser {

    List<censusDAO> censusList;
    public enum Country { INDIA, US }

    public CensusAnalyser() {
        this.censusList = new ArrayList<>();
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
        this.sortAscending(censusList, censusComparator);
        String sortedStateCensusJson = new Gson().toJson(censusList);
        return sortedStateCensusJson;
    }

    public String getStateWiseSortedStateCodeData() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw  new CensusAnalyserException("No Census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusComparator = Comparator.comparing(census -> census.stateCode);
        this.sortAscending(censusList, censusComparator);
        String sortedStateCodeCensusJson = new Gson().toJson(censusList);
        return sortedStateCodeCensusJson;
    }

    public String getStateWiseSortedCensusDataOnPopulation() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusCSVComparator = Comparator.comparing(census -> census.population);
        this.sortDescending(censusList, censusCSVComparator);
        String sortedPopulation = new Gson().toJson(censusList);
        return sortedPopulation;
    }

    public String getStateWiseSortedCensusDataOnPopulationDensity() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw  new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusCSVComparator = Comparator.comparing(census -> census.populationDensity);
        this.sortDescending(censusList, censusCSVComparator);
        String sortedPopulationDensity = new Gson().toJson(censusList);
        return sortedPopulationDensity;
    }

    public String getStateWiseSortedCensusDataOnArea() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw  new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusCSVComparator = Comparator.comparing(census -> census.totalArea);
        this.sortDescending(censusList, censusCSVComparator);
        String sortedAreaInSqKm = new Gson().toJson(censusList);
        return sortedAreaInSqKm;
    }

    public String getUSStateWiseSortedCensusDataOnPopulation() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No census data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusCSVComparator = Comparator.comparing(census -> census.population);
        this.sortDescending(censusList, censusCSVComparator);
        String sortedPopulation = new Gson().toJson(censusList);
        return sortedPopulation;
    }

    public String getUSStateWiseSortedCensusDataOnPopulationDensity() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw new CensusAnalyserException("No census data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusCSVComparator = Comparator.comparing(census -> census.populationDensity);
        this.sortDescending(censusList, censusCSVComparator);
        String sortedPopulationDensity = new Gson().toJson(censusList);
        return sortedPopulationDensity;
    }

    public String getUSStateWiseSortedCensusDataOnTotalArea() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<censusDAO> censusCSVComparator = Comparator.comparing(census -> census.totalArea);
        this.sortDescending(censusList, censusCSVComparator);
        String sortedTotalArea = new Gson().toJson(censusList);
        return sortedTotalArea;
    }

    private <E> void sortAscending( List<E> list, Comparator<E>censusComparator ) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - 1; j++) {
                E census1 = (E) list.get(j);
                E census2 = (E) list.get(j + 1);
                if (censusComparator.compare(census1, census2) > 0) {
                    list.set(j, census2);
                    list.set(j + 1, census1);
                }
            }
        }
    }

    private <E> void sortDescending( List<E> list, Comparator<E>censusComparator ) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - 1; j++) {
                E census1 = (E) list.get(j);
                E census2 = (E) list.get(j + 1);
                if (censusComparator.compare(census1, census2) < 0) {
                    list.set(j, census2);
                    list.set(j + 1, census1);
                }
            }
        }
    }

    public String mostPopulationDensityStateInIndiaAndUS() {
        Comparator<censusDAO> censusCSVComparator = Comparator.comparing(census -> census.populationDensity);
        this.sortDescending(censusList, censusCSVComparator);
        String sortedPopulationDensityUS = new Gson().toJson(censusList);
        return sortedPopulationDensityUS;
    }

}
