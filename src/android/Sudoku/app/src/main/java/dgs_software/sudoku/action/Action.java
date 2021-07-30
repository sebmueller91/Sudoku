package dgs_software.sudoku.action;

import java.util.UUID;

public abstract class Action {
    //region Attributes
    //region Identifier
    UUID m_identifier;
    public UUID getIdentifier() {
        return m_identifier;
    }
    public void setIdentifier(UUID identifier) {
        m_identifier = identifier;
    }
    //endregion
    //endregion

    // region Constructor
    public Action(UUID identifier) {
        setIdentifier(identifier);
    }

    public Action() {
        this(java.util.UUID.randomUUID());
    }
    // endregion

    // region Methods
    public void execute() {
        ActionStack.getInstance().addAction(this);
    }

    public void rollback() {
        undoAction();
        ActionStack.getInstance().removeAction(this);
    }

    protected abstract void undoAction();
    // endregion
}
