package censusanalyser.service;

import censusanalyser.exception.CensusAnalyserException;
import censusanalyser.models.censusDAO;

import java.util.List;

public class CensusAdapterFactory {
    public static List<censusDAO> getCensusData(CensusAnalyser.Country country, String... csvFilePath) throws CensusAnalyserException {
        if(country.equals(CensusAnalyser.Country.INDIA))
            return new IndiaCensusAdapter().loadCensusData(csvFilePath);
        if(country.equals(CensusAnalyser.Country.US))
            return new USCensusAdapter().loadCensusData(csvFilePath);
        throw new CensusAnalyserException("No Such Country", CensusAnalyserException.ExceptionType.NO_SUCH_COUNTRY);
    }
}
