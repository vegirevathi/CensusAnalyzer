package censusanalyser.service;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;
import censusanalyser.exception.CensusAnalyserException;
import censusanalyser.models.IndiaStateCode;
import censusanalyser.models.StateCensusCSV;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    List<StateCensusCSV> censusCSVList = null;
    List<IndiaStateCode> stateCSVList = null;


    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            List<StateCensusCSV> censusCSVList = csvBuilder.getCSVFileList(reader, StateCensusCSV.class);
            return censusCSVList.size();
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

    private <E> int getCount(Iterator<E> iterator) {
        Iterable<E> csviterable = () -> iterator;
        int numOfEnteries = (int) StreamSupport.stream(csviterable.spliterator(), false).count();
        return numOfEnteries;
    }

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if (censusCSVList == null || censusCSVList.size() == 0){
            throw  new CensusAnalyserException("No Census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<StateCensusCSV> censusComparator = Comparator.comparing(census -> census.state);
        this.sortAscending(censusComparator, censusCSVList);
        String sortedStateCensusJson = new Gson().toJson(censusCSVList);
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
        if (censusCSVList == null || censusCSVList.size() == 0){
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<StateCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.population);
        this.sortDescending(censusCSVComparator, censusCSVList);
        String sortedPopulation = new Gson().toJson(censusCSVList);
        return sortedPopulation;
    }

    public String getStateWiseSortedCensusDataOnPopulationDensity() throws CensusAnalyserException {
        if (censusCSVList == null || censusCSVList.size() == 0){
            throw  new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<StateCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.densityPerSqKm);
        this.sortDescending(censusCSVComparator, censusCSVList);
        String sortedPopulationDensity = new Gson().toJson(censusCSVList);
        return sortedPopulationDensity;
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
}
