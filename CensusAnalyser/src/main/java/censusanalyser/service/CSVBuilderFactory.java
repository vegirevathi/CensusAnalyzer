package censusanalyser.service;

public class CSVBuilderFactory {

    public static OpenCSVBuilder createCSVBuilder() {
        return new OpenCSVBuilder();
    }
}
