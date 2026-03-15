package dataviewer2;

import javax.swing.JOptionPane;

public class SetEndYearCommand implements Command {

    private DataViewer dv;
    private DebugManager debugManager;

    public SetEndYearCommand(DataViewer dv, DebugManager debugManager) {
        this.dv = dv;
        this.debugManager = debugManager;
    }

    @Override
    public void execute() {

        Object selectedValue = JOptionPane.showInputDialog(
                null,
                "Choose the end year",
                "Input",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                dv.getDataYears().toArray(),
                dv.getSelectedEndYear()
        );

        if (selectedValue != null) {

            debugManager.info("User seleted: '%s'", selectedValue);

            Integer year = (Integer) selectedValue;

            if (year.compareTo(dv.getSelectedStartYear()) < 0) {

                debugManager.error(
                        "new end year (%d) must be not be before start year (%d)",
                        year,
                        dv.getSelectedStartYear()
                );

            } else {

                if (!dv.getSelectedEndYear().equals(year)) {
                    dv.setSelectedEndYear(year);
                }

            }
        }
    }
}