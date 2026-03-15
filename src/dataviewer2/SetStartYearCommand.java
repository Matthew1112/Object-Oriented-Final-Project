package dataviewer2;

import javax.swing.JOptionPane;

public class SetStartYearCommand implements Command {

    private DataViewer dv;
    private DebugManager debugManager;

    public SetStartYearCommand(DataViewer dv, DebugManager debugManager) {
        this.dv = dv;
        this.debugManager = debugManager;
    }

    @Override
    public void execute() {

        Object selectedValue = JOptionPane.showInputDialog(
                null,
                "Choose the start year",
                "Input",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                dv.getDataYears().toArray(),
                dv.getSelectedStartYear()
        );

        if (selectedValue != null) {

            debugManager.info("User seleted: '%s'", selectedValue);

            Integer year = (Integer) selectedValue;

            if (year.compareTo(dv.getSelectedEndYear()) > 0) {

                debugManager.error(
                        "new start year (%d) must not be after end year (%d)",
                        year,
                        dv.getSelectedEndYear()
                );

            } else {

                if (!dv.getSelectedStartYear().equals(year)) {
                    dv.setSelectedStartYear(year);
                }

            }
        }
    }
}