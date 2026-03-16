package dataviewer2;

import java.util.List;

public interface TemperatureObserver {
	void onTemperatureRecord(List<Object> record, String state, Integer year);
}
