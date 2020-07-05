package censusanalyser.service;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;
import censusanalyser.exception.CensusAnalyserException;
import censusanalyser.models.IndiaStateCode;
import censusanalyser.models.StateCensusCSV;
import censusanalyser.models.USCensusCSV;
import censusanalyser.models.censusDAO;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    List<censusDAO> censusList;

    public CensusAnalyser() {
        this.censusList = new ArrayList<>();
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        return this.loadCensusData(csvFilePath, StateCensusCSV.class);
    }

    public int loadUSCensusData(String csvFilePath) throws CensusAnalyserException {
        return this.loadCensusData(csvFilePath, USCensusCSV.class);
    }

    public<E> int loadCensusData(String csvFilePath,   Class<E> csvClass) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> csvFileIterator = csvBuilder.getCSVFileIterator(reader, csvClass);
            Iterable<E> censusCSVIterable = () -> csvFileIterator;
            if (csvClass.getName().equals("censusanalyser.models.StateCensusCSV")) {
            StreamSupport.stream(censusCSVIterable.spliterator(), false)
                    .forEach( census -> censusList.add(new censusDAO((StateCensusCSV) census)));
            }else if (csvClass.getName().equals("censusanalyser.models.USCensusCSV")){
                StreamSupport.stream(censusCSVIterable.spliterator(), false)
                        .forEach( census -> censusList.add(new censusDAO((USCensusCSV) census)));
            }
            return this.censusList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.DELIMITER_HEADER_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(), e.type.name());
        }
    }

    public int loadIndiaStateCode(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCode> csvFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCode.class);
            Iterable<IndiaStateCode> censusCSVIterable = () -> csvFileIterator;
            StreamSupport.stream(censusCSVIterable.spliterator(), false)
                    .forEach( census -> censusList.add(new censusDAO(census)));
            return this.censusList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.DELIMITER_HEADER_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(), e.type.name());
        }
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

    public String mostPopulationDensityStateInIndiaAndUS(){
        Comparator<censusDAO> censusCSVComparator = Comparator.comparing(census -> census.populationDensity);
        this.sortDescending(censusList, censusCSVComparator);
        String sortedPopulationDensityUS = new Gson().toJson(censusList);
        return sortedPopulationDensityUS;
    }

}
