package censusanalyser.models;

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
}
