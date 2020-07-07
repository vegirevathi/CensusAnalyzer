package censusanalyser.service;

import censusanalyser.exception.CensusAnalyserException;
import censusanalyser.models.USCensusCSV;
import censusanalyser.models.censusDAO;

import java.util.List;

public class USCensusAdapter extends CensusAdapter {
    @Override
    public List<censusDAO> loadCensusData(String... csvFilePath) throws CensusAnalyserException {
        return super.loadCensusData(USCensusCSV.class, csvFilePath[0]);
    }
}
