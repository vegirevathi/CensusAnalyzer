package censusanalyser.models;

public class censusDAO {
    public int population;
    public Double populationDensity;
    public Double totalArea;
    public String state;

    public censusDAO(StateCensusCSV stateCensusCSV) {
         state = stateCensusCSV.state;
         totalArea = Double.valueOf(stateCensusCSV.areaInSqKm);
         //totalArea = stateCensusCSV.areaInSqKm;
         populationDensity = Double.valueOf(stateCensusCSV.densityPerSqKm);
         population = stateCensusCSV.population;
    }

    public censusDAO(USCensusCSV usCensusCSV) {
        state = usCensusCSV.state;
        population = usCensusCSV.population;
        populationDensity = usCensusCSV.populationDensity;
        totalArea = usCensusCSV.totalArea;
    }

}
