package censusanalyser.service;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;
import censusanalyser.exception.CensusAnalyserException;
import censusanalyser.models.IndiaStateCode;
import censusanalyser.models.StateCensusCSV;
import censusanalyser.models.USCensusCSV;
import censusanalyser.models.censusDAO;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusLoader {

    List<censusDAO> censusList;

    public CensusLoader() {
        this.censusList = new ArrayList<>();
    }

    public<E> List<censusDAO> loadCensusData(String csvFilePath, Class<E> csvClass) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> csvFileIterator = csvBuilder.getCSVFileIterator(reader, csvClass);
            Iterable<E> censusCSVIterable = () -> csvFileIterator;
            switch (csvClass.getName()) {
                case "censusanalyser.models.StateCensusCSV":
                    StreamSupport.stream(censusCSVIterable.spliterator(), false)
                            .forEach(census -> censusList.add(new censusDAO((StateCensusCSV) census)));
                    break;
                case "censusanalyser.models.USCensusCSV":
                    StreamSupport.stream(censusCSVIterable.spliterator(), false)
                            .forEach(census -> censusList.add(new censusDAO((USCensusCSV) census)));
                    break;
                case "censusanalyser.models.IndiaStateCode":
                    StreamSupport.stream(censusCSVIterable.spliterator(), false)
                            .forEach(census -> censusList.add(new censusDAO((IndiaStateCode) census)));
                    break;
            }
            return censusList;
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
}
