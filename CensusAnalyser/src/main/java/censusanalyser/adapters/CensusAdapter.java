package censusanalyser.adapters;

import CSVBuilder.CSVBuilderException;
import CSVBuilder.CSVBuilderFactory;
import CSVBuilder.ICSVBuilder;
import censusanalyser.exception.CensusAnalyserException;
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

public abstract class CensusAdapter {

    public abstract List<censusDAO>  loadCensusData(String...  csvFilePath) throws CensusAnalyserException;

    public <E> List<censusDAO> loadCensusData(Class<E> csvClass, String csvFilePath) throws CensusAnalyserException {
        List<censusDAO> censusList = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> csvFileIterator = csvBuilder.getCSVFileIterator(reader, csvClass);
            Iterable<E> censusCSVIterable = () -> csvFileIterator;
            if ("censusanalyser.models.StateCensusCSV".equals(csvClass.getName())) {
                StreamSupport.stream(censusCSVIterable.spliterator(), false)
                        .forEach(census -> censusList.add(new censusDAO((StateCensusCSV) census)));
            } else if ("censusanalyser.models.USCensusCSV".equals(csvClass.getName())) {
                StreamSupport.stream(censusCSVIterable.spliterator(), false)
                        .forEach(census -> censusList.add(new censusDAO((USCensusCSV) census)));
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
