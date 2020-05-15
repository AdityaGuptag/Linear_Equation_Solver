package solver.controller;

import solver.Row;
import solver.commands.Command;

public class Controller {
    private Command command;
    private Row otherRow;

    public void setCommandAndRow(Command command, Row row) {
        this.command = command;
        this.otherRow = row;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }
}
