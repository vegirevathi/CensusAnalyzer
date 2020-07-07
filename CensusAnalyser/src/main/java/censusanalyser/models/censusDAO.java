package censusanalyser.models;

import censusanalyser.service.CensusAnalyser;

public class censusDAO {
    public String stateCode;
    public int population;
    public double populationDensity;
    public double totalArea;
    public String state;

    public censusDAO(StateCensusCSV stateCensusCSV) {
         state = stateCensusCSV.state;
         totalArea = stateCensusCSV.areaInSqKm;
         populationDensity = stateCensusCSV.densityPerSqKm;
         population = stateCensusCSV.population;
        IndiaStateCode indiaStateCode = new IndiaStateCode();
        stateCode = indiaStateCode.StateCode;
    }

    public censusDAO(USCensusCSV usCensusCSV) {
        state = usCensusCSV.state;
        population = usCensusCSV.population;
        populationDensity = usCensusCSV.populationDensity;
        totalArea = usCensusCSV.totalArea;
        stateCode = usCensusCSV.stateId;
    }

    public censusDAO(IndiaStateCode indiaStateCode) {
         stateCode = indiaStateCode.StateCode;
         state = indiaStateCode.StateName;
    }

    public Object getCensusDTO(CensusAnalyser.Country country) {
        if (country.equals(CensusAnalyser.Country.US))
            return new USCensusCSV(state, stateCode, population, populationDensity, totalArea);
        return new StateCensusCSV(state, population, populationDensity, totalArea);
    }
}
