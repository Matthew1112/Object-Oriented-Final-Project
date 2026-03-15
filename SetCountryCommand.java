package dataviewer2;

import javax.swing.JOptionPane;

public class SetCountryCommand implements Command {
    private DataViewer dv;
    private DataLoader loader;
    private DebugManager debugManager;

    public SetCountryCommand(DataViewer dv, DataLoader loader, DebugManager debugManager) {
        this.dv = dv;
        this.loader = loader;
        this.debugManager = debugManager;
    }

    @Override
    public void execute() {
        Object selectedValue = JOptionPane.showInputDialog(
            null,
            "Choose a Country",
            "Input",
            JOptionPane.INFORMATION_MESSAGE,
            null,
            dv.getDataCountries().toArray(),
            dv.getSelectedCountry()
        );

        if (selectedValue != null) {
            debugManager.info("User selected country: '%s'", selectedValue);
            if (!selectedValue.equals(dv.getSelectedCountry())) {
                dv.setSelectedCountry((String) selectedValue);
                try {
                    loader.loadData();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}