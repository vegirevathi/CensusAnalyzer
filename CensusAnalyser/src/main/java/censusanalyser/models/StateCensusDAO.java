package censusanalyser.models;

public class StateCensusDAO {
    public int population;
    public int densityPerSqKm;
    public int areaInSqKm;
    public String state;

    public StateCensusDAO(StateCensusCSV stateCensusCSV) {
         state = stateCensusCSV.state;
         areaInSqKm = stateCensusCSV.areaInSqKm;
         densityPerSqKm = stateCensusCSV.densityPerSqKm;
         population = stateCensusCSV.population;
    }

}
