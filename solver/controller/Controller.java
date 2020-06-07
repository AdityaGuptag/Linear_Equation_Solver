package solver.controller;

import solver.Row;
import solver.commands.Command;

/**
 * This class is responsible for setting and executing different commands.
 * This acts like a remote controller for television or any other device to operate it
 */
public class Controller {
    private Command command;
    private Row otherRow;

    /**
     * Sets the command and the row if some other row is required for command completion
     * E.g. AddRowCommand
      * @param command
     * @param row
     */
    public void setCommand(Command command, Row row) {
        this.command = command;
        this.otherRow = row;
    }

    /**
     * Sets the command (Overloaded method)
     * @param command
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * Implementation of the command interface
     */
    public void executeCommand() {
        command.execute();
    }
}
