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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CensusAnalyser {

    List<StateCensusDAO> censusList = null;
    List<IndiaStateCode> stateCSVList;
    List<USCensusCSV> usCensusList;

    public CensusAnalyser() {
        this.censusList = new ArrayList<>();
        this.stateCSVList = new ArrayList<>();
        this.usCensusList = new ArrayList<>();
    }

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

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw  new CensusAnalyserException("No Census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<StateCensusDAO> censusComparator = Comparator.comparing(census -> census.state);
        this.sortAscending(censusList, censusComparator);
        String sortedStateCensusJson = new Gson().toJson(censusList);
        return sortedStateCensusJson;
    }

    public String getStateWiseSortedStateCodeData() throws CensusAnalyserException {
        if (stateCSVList == null || stateCSVList.size() == 0){
            throw  new CensusAnalyserException("No Census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaStateCode> stateCodeComparator = Comparator.comparing(state -> state.StateCode);
        this.sortAscending(stateCSVList, stateCodeComparator);
        String sortedStateCensusJson = new Gson().toJson(stateCSVList);
        return sortedStateCensusJson;
    }

    public String getStateWiseSortedCensusDataOnPopulation() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<StateCensusDAO> censusCSVComparator = Comparator.comparing(census -> census.population);
        this.sortDescending(censusList, censusCSVComparator);
        String sortedPopulation = new Gson().toJson(censusList);
        return sortedPopulation;
    }

    public String getStateWiseSortedCensusDataOnPopulationDensity() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw  new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<StateCensusDAO> censusCSVComparator = Comparator.comparing(census -> census.densityPerSqKm);
        this.sortDescending(censusList, censusCSVComparator);
        String sortedPopulationDensity = new Gson().toJson(censusList);
        return sortedPopulationDensity;
    }

    public String getStateWiseSortedCensusDataOnArea() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0){
            throw  new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<StateCensusDAO> censusCSVComparator = Comparator.comparing(census -> census.areaInSqKm);
        this.sortDescending(censusList, censusCSVComparator);
        String sortedAreaInSqKm = new Gson().toJson(censusList);
        return sortedAreaInSqKm;
    }

    public String getUSStateWiseSortedCensusDataOnPopulation() throws CensusAnalyserException {
        if (usCensusList == null || usCensusList.size() == 0) {
            throw new CensusAnalyserException("No census data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<USCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.population);
        this.sortDescending(usCensusList, censusCSVComparator);
        String sortedPopulation = new Gson().toJson(usCensusList);
        return sortedPopulation;
    }

    public String getUSStateWiseSortedCensusDataOnPopulationDensity() throws CensusAnalyserException {
        if (usCensusList == null || usCensusList.size() == 0){
            throw new CensusAnalyserException("No census data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<USCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.populationDensity);
        this.sortDescending(usCensusList, censusCSVComparator);
        String sortedPopulationDensity = new Gson().toJson(usCensusList);
        return sortedPopulationDensity;
    }

    public String getUSStateWiseSortedCensusDataOnTotalArea() throws CensusAnalyserException {
        if (usCensusList == null || usCensusList.size() == 0){
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<USCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.totalArea);
        this.sortDescending(usCensusList, censusCSVComparator);
        String sortedTotalArea = new Gson().toJson(usCensusList);
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

}
