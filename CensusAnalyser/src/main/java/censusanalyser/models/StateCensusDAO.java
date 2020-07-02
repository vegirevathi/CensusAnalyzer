package censusanalyser.models;

public class StateCensusDAO {
    public String population;
    public String densityPerSqKm;
    public String areaInSqKm;
    public String state;

    public StateCensusDAO(StateCensusCSV stateCensusCSV) {
         state = stateCensusCSV.state;
         areaInSqKm = stateCensusCSV.areaInSqKm;
         densityPerSqKm = stateCensusCSV.densityPerSqKm;
         population = stateCensusCSV.population;
    }

}
