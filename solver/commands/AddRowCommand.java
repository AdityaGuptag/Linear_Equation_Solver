package solver.commands;

import solver.Row;

//Concrete Command
public class AddRowCommand implements Command {
    private Row thisRow;
    private Row otherRow;

    public AddRowCommand (Row thisRow, Row otherRow) {
        this.thisRow = thisRow;
        this.otherRow = otherRow;
    }

    @Override
    public void execute() {
        this.thisRow.sumWithRow(otherRow);
    }
}
