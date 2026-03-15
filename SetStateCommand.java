package dataviewer2;

import javax.swing.JOptionPane;

public class SetStateCommand implements Command {
    private DataViewer dv;
    private DebugManager debugManager;

    public SetStateCommand(DataViewer dv, DebugManager debugManager) {
        this.dv = dv;
        this.debugManager = debugManager;
    }

    @Override
    public void execute() {
        Object selectedValue = JOptionPane.showInputDialog(
            null,
            "Choose a State",
            "Input",
            JOptionPane.INFORMATION_MESSAGE,
            null,
            dv.getDataStates().toArray(),
            dv.getSelectedState()
        );

        if (selectedValue != null) {
            debugManager.info("User selected state: '%s'", selectedValue);
            if (!selectedValue.equals(dv.getSelectedState())) {
                dv.setSelectedState((String) selectedValue);
            }
        }
    }
}