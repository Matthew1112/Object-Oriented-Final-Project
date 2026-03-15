package dataviewer2;

import javax.swing.JOptionPane;

public class SetVisualizationCommand implements Command {

    private DataViewer dv;
    private DebugManager debugManager;

    public SetVisualizationCommand(DataViewer dv, DebugManager debugManager) {
        this.dv = dv;
        this.debugManager = debugManager;
    }

    @Override
    public void execute() {

        Object selectedValue = JOptionPane.showInputDialog(
                null,
                "Choose the visualization mode",
                "Input",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Raw", "Extrema (within 10% of min/max)"},
                dv.getSelectedVisualization()
        );

        if (selectedValue != null) {

            debugManager.info("User seleted: '%s'", selectedValue);

            String visualization = (String) selectedValue;

            if (!dv.getSelectedVisualization().equals(visualization)) {
                dv.setSelectedVisualization(visualization);
            }
        }
    }
}