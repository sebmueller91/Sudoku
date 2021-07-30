package dgs_software.sudoku.action;

import java.util.UUID;

import dgs_software.sudoku.model.Cell;

public class SetCellValueAction extends Action {

    private Cell cell;
    private int value;
    private boolean isFixedValue;
    private int oldValue;
    private boolean oldIsFixedValue;

    public SetCellValueAction(UUID identifier, Cell cell, int value, boolean isFixedValue) {
        super(identifier);
        this.cell = cell;
        this.value = value;
        this.isFixedValue = isFixedValue;
    }

    public SetCellValueAction(Cell cell, int value, boolean isFixedValue) {
        this(java.util.UUID.randomUUID(), cell, value, isFixedValue);
    }

    @Override
    public void execute() {
        super.execute();
        if (cell != null) {
            oldValue = cell.getValue();
            cell.setValue(value);
            cell.setIsFixedValue(isFixedValue);
        }
    }

    @Override
    public void undoAction() {
        if (cell != null) {
            cell.setValue(oldValue);
            cell.setIsFixedValue(oldIsFixedValue);
        }
    }
}
