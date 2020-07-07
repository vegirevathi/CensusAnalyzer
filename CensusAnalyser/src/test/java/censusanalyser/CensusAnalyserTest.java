package censusanalyser;

import censusanalyser.exception.CensusAnalyserException;
import censusanalyser.models.StateCensusCSV;
import censusanalyser.models.USCensusCSV;
import censusanalyser.models.censusDAO;
import censusanalyser.service.CensusAnalyser;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String WRONG_FILE_TYPE = "./src/test/resources/IndiaStateCensusData.json";
    private static final String WRONG_DELIMITER = "./src/test/resources/IndiaStateCensusDataSample.csv";
    private static final String INDIA_STATE_CODE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String INDIA_STATE_CODE_WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCode.csv";
    private static final String INDIA_STATE_CODE_WRONG_CSV_FILE_TYPE = "./src/test/resources/IndiaStateCode.txt";
    private static final String INDIA_STATE_CODE_WRONG_DELIMITER = "./src/test/resources/IndiaStateCodeSample.csv";
    private static final String US_CENSUS_CSV_PATH = "./src/test/resources/USCensusData.csv";

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH);
            Assert.assertEquals(66, numOfRecords);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, WRONG_CSV_FILE_PATH, INDIA_STATE_CODE_WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFileExtension_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, WRONG_FILE_TYPE, INDIA_STATE_CODE_WRONG_CSV_FILE_TYPE);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongDelimiter_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, WRONG_DELIMITER, INDIA_STATE_CODE_WRONG_DELIMITER);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.DELIMITER_HEADER_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongHeader_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, WRONG_DELIMITER, INDIA_STATE_CODE_WRONG_DELIMITER);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.DELIMITER_HEADER_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndianStateCSVFile_Returns_CorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int stateCode = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH);
            Assert.assertEquals(66, stateCode);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnState_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getStateWiseSortedCensusData();
            StateCensusCSV[] stateCensusCsv = new Gson().fromJson(sortedCensusData, StateCensusCSV[].class);
            Assert.assertEquals("Andaman and Nicobar Islands", stateCensusCsv[0].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndianStateData_WhenSorted_OnStateCode_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH);
            String sortedCodeData = censusAnalyser.getStateWiseSortedStateCodeData();
            censusDAO[] indiaStateCode = new Gson().fromJson(sortedCodeData, censusDAO[].class);
            Assert.assertEquals("AD", indiaStateCode[0].stateCode);
            Assert.assertEquals("WB", indiaStateCode[36].stateCode);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndianCensusData_whenSorted_OnPopulation_ShouldReturnMostToLeast() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getStateWiseSortedCensusDataOnPopulation();
            StateCensusCSV[] stateCensusCsv = new Gson().fromJson(sortedCensusData, StateCensusCSV[].class);
            Assert.assertEquals(199812341, stateCensusCsv[0].population);
            Assert.assertEquals(607688, stateCensusCsv[28].population);
        } catch (CensusAnalyserException e) {
        }
    }

   @Test
    public void givenIndianCensusData_whenSorted_OnPopulationDensity_ShouldReturnMostToLeast() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getStateWiseSortedCensusDataOnPopulationDensity();
            censusDAO[] censusDAO = new Gson().fromJson(sortedCensusData, censusDAO[].class);
            Assert.assertEquals(1102.0, censusDAO[0].populationDensity, 0.000);
            Assert.assertEquals(50.0, censusDAO[28].populationDensity, 0.000);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenIndianCensusData_whenSorted_OnArea_ShouldReturnMostToLeast() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getStateWiseSortedCensusDataOnArea();
            censusDAO[] censusDAO= new Gson().fromJson(sortedCensusData, censusDAO[].class);
            Assert.assertEquals( 342239.0, censusDAO[0].totalArea, 0.0);
            Assert.assertEquals( 3702.0, censusDAO[28].totalArea, 0.0);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenUSCensusCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_PATH);
            Assert.assertEquals(51, numOfRecords);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenUsCensusData_WhenSortedOnPopulation_Highest_ShouldReturnSortedValue() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_PATH);
            String sortedUSCensusData = censusAnalyser.getUSStateWiseSortedCensusDataOnPopulation();
            USCensusCSV[] usCensusCSV = new Gson().fromJson(sortedUSCensusData, USCensusCSV[].class);
            Assert.assertEquals(37253956, usCensusCSV[0].population);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenUsCensusData_WhenSortedOn_PopulationDensity_ShouldReturnHighest() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_PATH);
            String sortedUSCensusData = censusAnalyser.getUSStateWiseSortedCensusDataOnPopulationDensity();
            USCensusCSV[] usCensusCSV = new Gson().fromJson(sortedUSCensusData, USCensusCSV[].class);
            Assert.assertEquals((Double) 3805.61, usCensusCSV[0].populationDensity);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenUsCensusData_WhenSortedOn_TotalArea_ShouldReturnHighest() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_PATH);
            String sortedUSCensusData = censusAnalyser.getUSStateWiseSortedCensusDataOnTotalArea();
            USCensusCSV[] usCensusCSV = new Gson().fromJson(sortedUSCensusData, USCensusCSV[].class);
            Assert.assertEquals((Double) 1723338.01, usCensusCSV[0].totalArea);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void mostPopulousStateAmongIndiaAndUS() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_PATH);
            String sortedCensusData = censusAnalyser.mostPopulationDensityStateInIndiaAndUS();
            censusDAO[] censusDAO = new Gson().fromJson(sortedCensusData, censusDAO[].class);
            Assert.assertEquals("District of Columbia", censusDAO[0].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

}
