package solver.commands;

import solver.Row;

/**
 * Concrete Command class
  */
public class AddRowCommand implements Command {
    private Row thisRow;
    private Row otherRow;

    /**
     * Sets the rows on which this command is to be executed
     * @param thisRow
     * @param otherRow
     */
    public AddRowCommand (Row thisRow, Row otherRow) {
        this.thisRow = thisRow;
        this.otherRow = otherRow;
    }

    /**
     * Executing the command
     * Overridden method of command interface
      */
    @Override
    public void execute() {
        this.thisRow.sumWithRow(otherRow);
    }
}
