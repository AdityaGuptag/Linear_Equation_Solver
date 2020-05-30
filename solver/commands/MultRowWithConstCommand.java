package solver.commands;

import solver.ComplexNumber;
import solver.Row;

public class MultRowWithConstCommand implements Command {
    private Row row;
    private ComplexNumber constant;
    public Row multRow;     //Stores the row object obtained after multiplying with the constant

    public MultRowWithConstCommand(Row row, ComplexNumber constant) {
        this.row = row;
        this.constant = constant;
    }

    @Override
    public void execute() {
        multRow = row.prodWithConst(this.constant);
    }
}
