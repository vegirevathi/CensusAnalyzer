package censusanalyser.exception;

public class CensusAnalyserException extends Exception {

    public enum ExceptionType {
         DELIMITER_HEADER_PROBLEM, NO_CENSUS_DATA, CENSUS_FILE_PROBLEM
    }

    public ExceptionType type;

    public CensusAnalyserException(String message, String name) {
        super(message);
        this.type = ExceptionType.valueOf(name);
    }

    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }

}
