package dataviewer2;

public class PlotDataCommand implements Command {
	private DataViewer dv;
	private PlotData plotData;
	
	public PlotDataCommand(DataViewer dv, PlotData plotData) {
		this.dv = dv;
		this.plotData = plotData;
		
	}
	@Override
	public void execute() {
		if(dv.getPlotData() == null) {
			plotData.updatePlotData();
		}
		
	}
	
}
