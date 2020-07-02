package censusanalyser.service;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;
import censusanalyser.exception.CensusAnalyserException;
import censusanalyser.models.IndiaStateCode;
import censusanalyser.models.StateCensusCSV;
import censusanalyser.models.StateCensusDAO;
import censusanalyser.models.USCensusCSV;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    List<StateCensusDAO> censusList = null;
    List<IndiaStateCode> stateCSVList = null;
    List<USCensusCSV> usCensusList = null;

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<StateCensusCSV> csvFileIterator = csvBuilder.getCSVFileIterator(reader, StateCensusCSV.class);
            while (csvFileIterator.hasNext()) {
                this.censusList.add(new StateCensusDAO(csvFileIterator.next()));
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
            List<IndiaStateCode> stateCSVList = csvBuilder.getCSVFileList(reader, IndiaStateCode.class);
            return stateCSVList.size();
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

    public int loadUSCensusData(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            usCensusList = csvBuilder.getCSVFileList(reader, USCensusCSV.class);
            return usCensusList.size();
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

    private <E> int getCount(Iterator<E> iterator) {
        Iterable<E> csviterable = () -> iterator;
        int numOfEnteries = (int) StreamSupport.stream(csviterable.spliterator(), false).count();
        return numOfEnteries;
    }

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw  new CensusAnalyserException("No Census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<StateCensusDAO> censusComparator = Comparator.comparing(census -> census.state);
        this.sortAscending(censusComparator, censusList);
        String sortedStateCensusJson = new Gson().toJson(censusList);
        return sortedStateCensusJson;
    }

    public String getStateWiseSortedStateCodeData() throws CensusAnalyserException {
        if (stateCSVList == null || stateCSVList.size() == 0){
            throw  new CensusAnalyserException("No Census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaStateCode> stateCodeComparator = Comparator.comparing(state -> state.StateCode);
        this.sortAscending(stateCodeComparator, stateCSVList);
        String sortedStateCensusJson = new Gson().toJson(stateCSVList);
        return sortedStateCensusJson;
    }

    public String getStateWiseSortedCensusDataOnPopulation() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<StateCensusDAO> censusCSVComparator = Comparator.comparing(census -> census.population);
        this.sortDescending(censusCSVComparator, censusList);
        String sortedPopulation = new Gson().toJson(censusList);
        return sortedPopulation;
    }

    public String getStateWiseSortedCensusDataOnPopulationDensity() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw  new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<StateCensusDAO> censusCSVComparator = Comparator.comparing(census -> census.densityPerSqKm);
        this.sortDescending(censusCSVComparator, censusList);
        String sortedPopulationDensity = new Gson().toJson(censusList);
        return sortedPopulationDensity;
    }

    public String getStateWiseSortedCensusDataOnArea() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw  new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<StateCensusDAO> censusCSVComparator = Comparator.comparing(census -> census.areaInSqKm);
        this.sortDescending(censusCSVComparator, censusList);
        String sortedAreaInSqKm = new Gson().toJson(censusList);
        return sortedAreaInSqKm;
    }

    private <E> void sortAscending( Comparator<E>censusComparator, List<E> list ) {
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

    private <E> void sortDescending(Comparator<E> censusComparator, List<E> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - 1; j++) {
                E census1 = (E) list.get(j);
                E census2 = (E) list.get(j + 1);
                if (censusComparator.compare(census1, census2) < 0) {
                    list.set(j, census1);
                    list.set(j + 1, census2);
                }
            }
        }
    }

    public String getUSStateWiseSortedCensusDataOnPopulation() throws CensusAnalyserException {
        if (usCensusList == null || usCensusList.size() == 0){
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<USCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.population);
        this.sortDescending(censusCSVComparator, usCensusList);
        String sortedPopulation = new Gson().toJson(usCensusList);
        return sortedPopulation;
    }

    public String getUSStateWiseSortedCensusDataOnPopulationDensity() throws CensusAnalyserException {
        if (usCensusList == null || usCensusList.size() == 0){
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<USCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.populationDensity);
        this.sortDescending(censusCSVComparator, usCensusList);
        String sortedPopulation = new Gson().toJson(usCensusList);
        return sortedPopulation;
    }

    public String getUSStateWiseSortedCensusDataOnTotalArea() throws CensusAnalyserException {
        if (usCensusList == null || usCensusList.size() == 0){
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<USCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.populationDensity);
        this.sortDescending(censusCSVComparator, usCensusList);
        String sortedPopulation = new Gson().toJson(usCensusList);
        return sortedPopulation;
    }
}
