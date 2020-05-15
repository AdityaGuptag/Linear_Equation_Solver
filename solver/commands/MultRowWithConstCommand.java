package solver.commands;

import solver.Row;

public class MultRowWithConstCommand implements Command {
    private Row row;
    private double constant;
    public Row multRow;     //Stores the row object obtained after multiplying with the constant

    public MultRowWithConstCommand(Row row, double constant) {
        this.row = row;
        this.constant = constant;
    }

    @Override
    public void execute() {
        multRow = row.prodWithConst(this.constant);
    }
}
