package dgs_software.sudoku.action;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActionStack {
    // region Singleton Definition
    private static final ActionStack INSTANCE = new ActionStack();

    private ActionStack() {
        actions = new ArrayList<Action>();
    }

    public static ActionStack getInstance() {
        return INSTANCE;
    }
    // region

    private List<Action> actions;

    // region Methods
    public void addAction(Action action) {
        actions.add(action);
    }
    public void removeAction(Action action) {
        actions.remove(action);
    }

    public void rollbackActions() {
        if (actions == null || actions.size() == 0) {
            return;
        }
        UUID lastActionIdentifier = actions.get(actions.size()-1).getIdentifier();
        for (int i = actions.size()-1; i >= 0; i--) {
            if (actions.get(i).getIdentifier().equals(lastActionIdentifier)) {
                actions.get(i).rollback();
            }
        }
    }

    public void resetActions() {
        getInstance().actions.clear();
    }

    public boolean actionsHistoryNotEmpty() {
        return actions.size() > 0;
    }

    public void doMultipleActions(List<Action> newActions) {
        UUID jointIdentifier = java.util.UUID.randomUUID();

        for (Action action : newActions) {
            action.setIdentifier(jointIdentifier);
            action.execute();
        }
    }
    // endregion
}
