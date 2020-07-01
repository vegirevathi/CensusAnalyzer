package censusanalyser.service;

import censusanalyser.exception.CensusAnalyserException;
import censusanalyser.models.IndiaStateCode;
import censusanalyser.models.StateCensusCSV;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<StateCensusCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, StateCensusCSV.class);
            return this.getCount(censusCSVIterator);
        } catch (IOException e) {
                throw new CensusAnalyserException(e.getMessage(),
                        CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.DELIMITER_HEADER_PROBLEM);
        }
    }

    public int loadIndiaStateCode(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCode> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCode.class);
            return this.getCount(censusCSVIterator);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.DELIMITER_HEADER_PROBLEM);
        }
    }

    private <E> int getCount(Iterator<E> iterator) {
        Iterable<E> csviterable = () -> iterator;
        int numOfEnteries = (int) StreamSupport.stream(csviterable.spliterator(), false).count();
        return numOfEnteries;
    }
}
