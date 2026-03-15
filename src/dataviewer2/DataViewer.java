package dataviewer2;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import edu.du.dudraw.DrawListener;

public class DataViewer implements DrawListener {

    private final static String DEFAULT_COUNTRY = "United States";
    private final static String[] VISUALIZATION_MODES = {"Raw", "Extrema (within 10% of min/max)"};

    private final static int GUI_MODE_MAIN_MENU = 0;
    private final static int GUI_MODE_DATA = 1;

    // data storage
    private List<List<Object>> m_dataRaw;
    private SortedSet<String> m_dataStates;
    private SortedSet<String> m_dataCountries;
    private SortedSet<Integer> m_dataYears;

    // user selections
    private String m_selectedCountry = DEFAULT_COUNTRY;
    private Integer m_selectedEndYear;
    private String m_selectedState;
    private Integer m_selectedStartYear;
    private String m_selectedVisualization = VISUALIZATION_MODES[0];

    // plot-related data
    private TreeMap<Integer, Double> m_plotMonthlyMaxValue = null;
    private TreeMap<Integer, Double> m_plotMonthlyMinValue = null;

    // GUI-related settings
    private int m_guiMode = GUI_MODE_MAIN_MENU; // Menu by default

    // plot-related data
    private TreeMap<Integer, SortedMap<Integer, Double>> m_plotData = null;

    private DrawManager drawManager;
    private DataLoader loader;
    private DebugManager debugManager;
    private PlotData plotData;
    private TemperatureRecord tempRecord;

    /**
     * Constructor sets up the window and loads the specified data file.
     */
    public DataViewer(String dataFile) throws FileNotFoundException {
        debugManager = DebugManagerFactory.createVerbose();
        drawManager = new DrawManager(this, this, debugManager);
        plotData = new PlotData(this);
        tempRecord = new TemperatureRecord(this, debugManager);

        // Load data
        loader = new DataLoader(this, dataFile, tempRecord, debugManager);
        loader.loadData();

        // draw the screen for the first time -- this will be the main menu
        update();
    }

    public List<List<Object>> getDataRaw() {
        return m_dataRaw;
    }

    public void setDataRaw(List<List<Object>> m_dataRaw) {
        this.m_dataRaw = m_dataRaw;
    }

    public SortedSet<String> getDataStates() {
        return m_dataStates;
    }

    public void setDataStates(SortedSet<String> m_dataStates) {
        this.m_dataStates = m_dataStates;
    }

    public SortedSet<String> getDataCountries() {
        return m_dataCountries;
    }

    public void setDataCountries(SortedSet<String> m_dataCountries) {
        this.m_dataCountries = m_dataCountries;
    }

    public SortedSet<Integer> getDataYears() {
        return m_dataYears;
    }

    public void setDataYears(SortedSet<Integer> m_dataYears) {
        this.m_dataYears = m_dataYears;
    }

    public String getSelectedCountry() {
        return m_selectedCountry;
    }

    public void setSelectedCountry(String m_selectedCountry) {
		  this.m_selectedCountry = m_selectedCountry;
	}

	public Integer getSelectedEndYear() {
        return m_selectedEndYear;
    }

    public void setSelectedEndYear(Integer m_selectedEndYear) {
        this.m_selectedEndYear = m_selectedEndYear;
    }

    public String getSelectedState() {
        return m_selectedState;
    }

    public void setSelectedState(String m_selectedState) {
        this.m_selectedState = m_selectedState;
    }

    public Integer getSelectedStartYear() {
        return m_selectedStartYear;
    }

    public void setSelectedStartYear(Integer m_selectedStartYear) {
        this.m_selectedStartYear = m_selectedStartYear;
    }

    public String getSelectedVisualization() {
        return m_selectedVisualization;
    }
    

    public void setSelectedVisualization(String m_selectedVisualization) {
		  this.m_selectedVisualization = m_selectedVisualization;
	}

	public TreeMap<Integer, Double> getPlotMonthlyMaxValue() {
        return m_plotMonthlyMaxValue;
    }

    public void setPlotMonthlyMaxValue(TreeMap<Integer, Double> m_plotMonthlyMaxValue) {
        this.m_plotMonthlyMaxValue = m_plotMonthlyMaxValue;
    }

    public TreeMap<Integer, Double> getPlotMonthlyMinValue() {
        return m_plotMonthlyMinValue;
    }

    public void setPlotMonthlyMinValue(TreeMap<Integer, Double> m_plotMonthlyMinValue) {
        this.m_plotMonthlyMinValue = m_plotMonthlyMinValue;
    }

    public TreeMap<Integer, SortedMap<Integer, Double>> getPlotData() {
        return m_plotData;
    }

    public void setPlotData(TreeMap<Integer, SortedMap<Integer, Double>> m_plotData) {
        this.m_plotData = m_plotData;
    }

    @Override
    public void update() {
        if (m_guiMode == GUI_MODE_MAIN_MENU) {
            drawManager.drawMainMenu();
        } else if (m_guiMode == GUI_MODE_DATA) {
            drawManager.drawData(m_plotData);
        } else {
            throw new IllegalStateException(String.format("Unexpected drawMode=%d", m_guiMode));
        }
        // for double-buffering
        drawManager.getWindow().show();

    }

    // Below are the mouse/key listeners

    /**
     * Handle key press.  Q always quits.  Otherwise process based on GUI mode.
     */
    @Override
    public void keyPressed(int key) {
        boolean needsUpdate = false;
        boolean needsUpdatePlotData = false;
        debugManager.trace("key pressed '%c'", (char) key);

        // Quit immediately
        if (key == 'Q') {
            System.out.println("Bye");
            System.exit(0);
        }

        Command cmd = null;

        if (m_guiMode == GUI_MODE_MAIN_MENU) {
            switch (key) {
                case 'P':
                    // Switch to data view
                    m_guiMode = GUI_MODE_DATA;
                    needsUpdate = true;
                    needsUpdatePlotData = (m_plotData == null);
                    break;
                case 'C':
                    cmd = new SetCountryCommand(this, loader, debugManager);
                    break;
                case 'T':
                    cmd = new SetStateCommand(this, debugManager);
                    break;
                case 'S':
                    cmd = new SetStartYearCommand(this, debugManager);
                    break;
                case 'E':
                    cmd = new SetEndYearCommand(this, debugManager);
                    break;
                case 'V':
                    cmd = new SetVisualizationCommand(this, debugManager);
                    break;
                default:
                    debugManager.trace("No action for key '%c'", (char) key);
            }
        } else if (m_guiMode == GUI_MODE_DATA) {
            if (key == 'M') {
                // Go back to main menu
                m_guiMode = GUI_MODE_MAIN_MENU;
                needsUpdate = true;
            }
        } else {
            throw new IllegalStateException(String.format("unexpected mode: %d", m_guiMode));
        }

        // Execute the command if one was created
        if (cmd != null) {
            cmd.execute();
            needsUpdate = true;
            // Commands that affect the plot
            if (cmd instanceof SetCountryCommand || cmd instanceof SetStateCommand
                || cmd instanceof SetStartYearCommand || cmd instanceof SetEndYearCommand) {
                needsUpdatePlotData = true;
            }
        }

        if (needsUpdatePlotData) {
            plotData.updatePlotData();
        }

        if (needsUpdate) {
            update();
        }
    }

    @Override
    public void keyReleased(int key) {
    }

    @Override
    public void keyTyped(char key) {
    }

    @Override
    public void mouseClicked(double x, double y) {
    }

    @Override
    public void mouseDragged(double x, double y) {
    }

    @Override
    public void mousePressed(double x, double y) {
    }

    @Override
    public void mouseReleased(double x, double y) {
    }

}
