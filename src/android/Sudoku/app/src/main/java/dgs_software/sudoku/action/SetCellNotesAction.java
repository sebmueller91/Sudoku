package dgs_software.sudoku.action;

import java.util.UUID;

import dgs_software.sudoku.model.Cell;
import dgs_software.sudoku.utils.Utils;

public class SetCellNotesAction extends Action {

    private Cell cell;
    private boolean[] value;
    private boolean[] oldValue;

    public SetCellNotesAction(UUID identifier, Cell cell, boolean[] value) {
        super(identifier);
        this.cell = cell;
        this.value = value;
    }

    public SetCellNotesAction(Cell cell, boolean[] value) {
        this(java.util.UUID.randomUUID(), cell, value);
    }

    @Override
    public void execute() {
        super.execute();
        if (cell != null) {
            oldValue = Utils.createCopyOfBoolArray(cell.getActiveNotes());
            cell.setActiveNotes(value);
        }
    }

    @Override
    public void undoAction() {
        if (cell != null) {
            cell.setActiveNotes(oldValue);
        }
    }
}
