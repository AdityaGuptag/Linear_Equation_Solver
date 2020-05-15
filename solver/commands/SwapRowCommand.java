package solver.commands;

import solver.Row;

public class SwapRowCommand implements Command{
    private Row thisRow;
    private Row otherRow;
    public SwapRowCommand(Row thisRow, Row otherRow) {
        this.thisRow = thisRow;
        this.otherRow = otherRow;
    }

    @Override
    public void execute() {
        thisRow.swapRows(otherRow);
    }
}
