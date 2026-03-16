package dataviewer2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;

public class TemperatureRecord {
	
	
	private DebugManager debugManager;
	private DataViewer dv;
	private TemperatureRecordFactory factory;
	private List<TemperatureObserver> observers = new ArrayList<>();
	
	private final static int FILE_COUNTRY_IDX = 4;
	private final static int FILE_NUM_COLUMNS = 5;
	
	public TemperatureRecord(DataViewer dv, DebugManager debugManager) {
		this.dv = dv;
		this.debugManager = debugManager;
		factory = new TemperatureRecordFactory();
	}

	/**
     * Utility function to pull a year integer out of a date string.  Supports M/D/Y and Y-M-D formats only.
     * 
     * @param dateString
     * @return
     */
    public Integer parseYear(String dateString) {
    	Integer ret = null;
    	if(dateString.indexOf("/") != -1) {
    		// Assuming something like 1/20/1823
    		String[] parts = dateString.split("/");
    		if(parts.length == 3) {
	    		ret = Integer.parseInt(parts[2]);
    		}
    	}
    	else if(dateString.indexOf("-") != -1) {
    		// Assuming something like 1823-01-20
    		String[] parts = dateString.split("-");
    		if(parts.length == 3) {
    			ret = Integer.parseInt(parts[0]);
    		}
    	}
    	else {
    		throw new RuntimeException(String.format("Unexpected date delimiter: '%s'", dateString));
    	}
    	if(ret == null) {
    		debugManager.trace("Unable to parse year from date: '%s'", dateString);
    	}
    	return ret;
    }
    
    public Integer parseMonth(String dateString) {
    	Integer ret = null;
    	if(dateString.indexOf("/") != -1) {
    		// Assuming something like 1/20/1823
    		String[] parts = dateString.split("/");
    		if(parts.length == 3) {
	    		ret = Integer.parseInt(parts[0]);
    		}
    	}
    	else if(dateString.indexOf("-") != -1) {
    		// Assuming something like 1823-01-20
    		String[] parts = dateString.split("-");
    		if(parts.length == 3) {
    			ret = Integer.parseInt(parts[1]);
    		}
    	}
    	else {
    		throw new RuntimeException(String.format("Unexpected date delimiter: '%s'", dateString));
    	}
    	if(ret == null || ret.intValue() < 1 || ret.intValue() > 12) {
    		debugManager.trace("Unable to parse month from date: '%s'", dateString);
    		return null;
    	}
    	return ret;
	}
    
    public List<Object> getRecordFromLine(String line) {
        List<String> rawValues = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                rawValues.add(rowScanner.next());
            }
        }
        
        dv.getDataCountries().add(rawValues.get(FILE_COUNTRY_IDX));
        if(rawValues.size() != FILE_NUM_COLUMNS) {
        	debugManager.trace("malformed line '%s'...skipping", line);
        	return null;
        }
        else if(!rawValues.get(FILE_COUNTRY_IDX).equals(dv.getSelectedCountry())) {
        	debugManager.trace("skipping non-USA record: %s", rawValues);
        	return null;
        }
        else {
        	debugManager.trace("processing raw data: %s", rawValues.toString());
        }
        try {
        	
        	List<Object> record = factory.createRecord(this, rawValues, dv);
        	
        	if(record != null) {
        		
        		Integer year = (Integer)record.get(0);
        		String state = (String)record.get(3);
        		notifyObservers(record, state, year);
        	}
        	
        	return record;
     
        }
        catch(NumberFormatException e) {
        	debugManager.trace("unable to parse data line, skipping...'%s'", line);
        	return null;
        }
    }
    
    public void addObserver(TemperatureObserver observer) {
    	observers.add(observer);
    }
    public void removeObserver(TemperatureObserver observer) {
    	observers.remove(observer);
    }
    public void notifyObservers(List<Object> record, String state, Integer year) {
    	for(TemperatureObserver o: observers) {
    		o.onTemperatureRecord(record, state, year);
    	}
    }
}
