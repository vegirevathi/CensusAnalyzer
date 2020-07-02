package censusanalyser.models;
import com.opencsv.bean.CsvBindByName;

public class IndiaStateCode {

    @CsvBindByName(column = "State Name", required = true)
    public String StateName;

    @CsvBindByName(column = "StateCode", required = true)
    public String StateCode;

    @Override
    public String toString() {
        return "IndiaStateCode{" +
                "StateName='" + StateName + '\'' +
                ", StateCode=" + StateCode +
                '}';
    }
}
