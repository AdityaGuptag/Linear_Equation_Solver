package solver.commands;

import solver.Row;

/**
 * Concrete Command class
 */
public class SwapColumnCommand implements Command {
    private Row row;
    private int col1;
    private int col2;

    /**
     * Sets the row, and its columns on which command is to be executed
     * @param row
     * @param col1
     * @param col2
     */
    public SwapColumnCommand(Row row, int col1, int col2) {
        this.row = row;
        this.col1 = col1;
        this.col2 = col2;
    }

    /**
     * Executing the command
     * Overridden method of command interface
     */
    @Override
    public void execute() {
        row.swapColumns(col1, col2);
    }
}
