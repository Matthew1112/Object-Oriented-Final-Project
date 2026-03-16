package dataviewer2;

import java.util.ArrayList;
import java.util.List;

public class TemperatureRecordFactory {
	
	private final static int FILE_DATE_IDX = 0;
	private final static int FILE_STATE_IDX = 3;
	private final static int FILE_TEMPERATURE_IDX = 1;

	public List<Object> createRecord(TemperatureRecord record, List<String> rawValues) {
		// Parse these into more useful objects than String
    	List<Object> values = new ArrayList<Object>(4);
    	
    	Integer year = record.parseYear(rawValues.get(FILE_DATE_IDX));
    	if(year == null) {
    		return null;
    	}
    	values.add(year);
    	
    	Integer month = record.parseMonth(rawValues.get(FILE_DATE_IDX));
    	if(month == null) {
    		return null;
    	}
    	values.add(month);
    	values.add(Double.parseDouble(rawValues.get(FILE_TEMPERATURE_IDX)));
    	//not going to use UNCERTAINTY yet
    	//values.add(Double.parseDouble(rawValues.get(FILE_UNCERTAINTY_IDX)));
    	values.add(rawValues.get(FILE_STATE_IDX));
    	// since all are the same country
    	//values.add(rawValues.get(FILE_COUNTRY_IDX));
    	
    	// if we got here, add the state to the list of states
    	//dv.getDataStates().add(rawValues.get(FILE_STATE_IDX));
    	//dv.getDataYears().add(year);
    	return values;
	}
	
}
