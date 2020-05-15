package solver.commands;

import solver.Row;

public class SwapColumnCommand implements Command {
    private Row row;
    private int col1;
    private int col2;

    public SwapColumnCommand(Row row, int col1, int col2) {
        this.row = row;
        this.col1 = col1;
        this.col2 = col2;
    }

    @Override
    public void execute() {
        row.swapColumns(col1, col2);
    }
}
