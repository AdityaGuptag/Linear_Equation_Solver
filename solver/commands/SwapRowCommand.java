package solver.commands;

import solver.Row;

/**
 * Concrete command class
 */
public class SwapRowCommand implements Command{
    private Row thisRow;
    private Row otherRow;

    /**
     * Sets the rows which are to be swapped
     * @param thisRow
     * @param otherRow
     */
    public SwapRowCommand(Row thisRow, Row otherRow) {
        this.thisRow = thisRow;
        this.otherRow = otherRow;
    }

    /**
     * Executing the command
     * Overridden method of command interface
     */
    @Override
    public void execute() {
        thisRow.swapRows(otherRow);
    }
}
