package censusanalyser.exception;

public class CensusAnalyserException extends Exception {

    public enum ExceptionType {
        UNABLE_TO_PARSE, FILE_NOT_FOUND, DELIMITER_HEADER_PROBLEM, CENSUS_FILE_PROBLEM
    }

    public ExceptionType type;

    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }

    public CensusAnalyserException(String message, ExceptionType type, Throwable cause) {
        super(message, cause);
        this.type = type;
    }
}
