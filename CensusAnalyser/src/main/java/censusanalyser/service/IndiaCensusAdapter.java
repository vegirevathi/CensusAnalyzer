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

public class IndiaAdapter {

    public List<censusDAO> loadCensusData(String... csvFilePath) throws CensusAnalyserException {
        List<censusDAO> censusList = super.loadCensusData(StateCensusCSV.class,csvFilePath[0]);
        this.loadIndiaStateCode(censusList, csvFilePath[1]);
        return censusList;
    }



    private int loadIndiaStateCode(List<censusDAO> censusList, String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCode> csvFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCode.class);
            Iterable<IndiaStateCode> censusCSVIterable = () -> csvFileIterator;
            StreamSupport.stream(censusCSVIterable.spliterator(), false)
                    .forEach( census -> censusList.add(new censusDAO(census)));
            return censusList.size();
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
