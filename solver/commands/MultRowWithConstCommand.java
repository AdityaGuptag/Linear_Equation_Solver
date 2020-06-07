package solver.commands;

import solver.Row;

/**
 * Concrete command
 */
public class MultRowWithConstCommand implements Command {
    private Row row;
    private double constant;
    public Row multRow;     //Stores the row object obtained after multiplying with the constant

    /**
     * To multiply the input row with the input constant value
     * @param row
     * @param constant
     */
    public MultRowWithConstCommand(Row row, double constant) {
        this.row = row;
        this.constant = constant;
    }

    /**
     * Executing the command
     * Overridden method of command interface
     */
    @Override
    public void execute() {
        multRow = row.prodWithConst(this.constant);
    }
}
