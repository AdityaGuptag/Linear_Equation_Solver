package solver;

import solver.commands.AddRowCommand;
import solver.commands.MultRowWithConstCommand;
import solver.commands.SwapColumnCommand;
import solver.commands.SwapRowCommand;
import solver.controller.Controller;

import java.util.Arrays;
import java.util.Stack;

/**
 * Client class that receives all the commands from the controller class
 */
public class Matrix {
    private Controller controller;
    public Row[] matrixRow;     // Array of Row objects. Each object stores a linear equation
    public static int numVars;  // Stores the number of variables read from input file
    public static int numEquations; // Stores the number of equations read from input file
    private int currRowNum = 0; // To maintain the count of current row while adding row in matrix
    private Stack<Integer[]> columnsSwap;
    public double[] solArr;     // Stores the final solutions of the solved system
    String ans;                 /*
                                   If there is no solution to the system, it stores "No Solution"
                                   and if infinite solutions exist, it stores "Infinitely many solutions"
                                */
    private int significantEq;      // Stores the number equations which are not all 0 rows

    public Matrix (int noOfVars, int numOfEquations) {
        numVars = noOfVars;
        numEquations = numOfEquations;
        this.controller = new Controller();
        this.matrixRow = new Row[numEquations];
        for (int i = 0; i < numEquations; i++) {
            matrixRow[i] = new Row(numVars);
        }
        this.columnsSwap = new Stack<Integer[]>();
        this.solArr = new double[numVars];
        this.ans = "";
        this.significantEq = 0;
    }

    /**
     * Adds the elements array read from input file one-by-one from each of its line into row abjects
     * @param elements
     */
    public void addRowInMatrix (double[] elements) {
        matrixRow[currRowNum++].generateRow(elements);
        if (!matrixRow[currRowNum - 1].allZeroRow) {
            this.significantEq++;
        }
    }

    /***
     * Function responsible for solving the linear Equations which are stored in Matrix object
     */
    public void solveEquations () {

        // To make the elements of lower triangular matrix = 0 and make the diagonal entries = 1
        for (int i = 0; i < numEquations - 1; i++) {
            boolean interchanged = doSplOps(matrixRow[i], i);
            if (this.ans.equals("No solutions") || this.ans.equals("Infinitely many solutions")) {
                return;
            }
            for (int j = i + 1; j < numEquations; j++) {
                // Just to print the row operation carried out
                double constant = -1 * matrixRow[j].elementsArr[i] / matrixRow[i].elementsArr[i];
                if (constant != 0.0) {
                    System.out.println(constant + " * R" + i + " + R" + j + " --> " + "R" + j);

                    MultRowWithConstCommand multRowWithConstCommand = new MultRowWithConstCommand(matrixRow[i],
                            constant);
                    controller.setCommand(multRowWithConstCommand);
                    controller.executeCommand();
                    Row tempRow = multRowWithConstCommand.multRow;

                    AddRowCommand addRowCommand = new AddRowCommand(matrixRow[j], tempRow);
                    controller.setCommand(addRowCommand, tempRow);
                    controller.executeCommand();
                }
            }

            // To make the pivot element of current row = 1
            if (matrixRow[i].elementsArr[i] != 1) {
                double constant = 1 / matrixRow[i].elementsArr[i];
                System.out.println(constant + " * R" + i + " --> " + "R" + i);
                MultRowWithConstCommand multRowWithConstCommand = new MultRowWithConstCommand(matrixRow[i],
                        constant);
                controller.setCommand(multRowWithConstCommand);
                controller.executeCommand();
                matrixRow[i] = multRowWithConstCommand.multRow;
            }

            // To make the pivot element of last row  = 1
            if (i == numVars - 2 && matrixRow[i + 1].elementsArr[i + 1] != 1) {
                interchanged = doSplOps(matrixRow[i + 1], i + 1);
                if (this.ans.equals("No solutions") || this.ans.equals("Infinitely many solutions")) {
                    return;
                }
                double constant = 1 / matrixRow[numVars - 1].elementsArr[numVars - 1];
                System.out.println( constant + " * R" + (i + 1) + " --> " + "R" + (i + 1));
                MultRowWithConstCommand multRowWithConstCommand = new MultRowWithConstCommand(matrixRow[numVars - 1],
                        constant);
                controller.setCommand(multRowWithConstCommand);
                controller.executeCommand();
                matrixRow[numVars - 1] = multRowWithConstCommand.multRow;
            }
        }

        if (significantEq < numVars) {
            for (int i = significantEq - 1; i >= 0; i--) {
                int cnt = 0;
                for (int j = 0; j < numVars; j++) {
                    if (roundoff(matrixRow[i].elementsArr[j]) == 0) {
                        cnt++;
                    }
                }
                if (cnt == numVars && roundoff(matrixRow[i].elementsArr[numVars]) != 0) {
                    matrixRow[i].elementsArr[numVars] = roundoff(matrixRow[i].elementsArr[numVars]);
                    this.ans = "No solutions";
                    return;
                }
            }
            this.ans = "Infinitely many solutions";
            return;
        } else {
            // To check the equations below the last equation in which pivot element was made = 1
            for (int i = numVars; i < numEquations; i++) {
                if (roundoff(matrixRow[i].elementsArr[numVars]) != 0) {
                    matrixRow[i].elementsArr[numVars] = roundoff(matrixRow[i].elementsArr[numVars]);
                    this.ans = "No solutions";
                    return;
                }
            }

            // To make the elements of upper triangular matrix equal 0
            for (int i = numVars - 1; i > 0; i--) {
                for (int j = i - 1; j >= 0; j--) {
                    double constant = -1 * matrixRow[j].elementsArr[i] / matrixRow[i].elementsArr[i];
                    if (roundoff(constant) != 0.0) {
                        System.out.println(constant + " * R" + i + " + R" + j + " --> " + "R" + j);
                        matrixRow[j].sumWithRow(matrixRow[i].prodWithConst(constant));

                    }

                }
            }
        }
        reverseColSwap();
    }

    public void printSolution() {
        for (int i = 0; i < numVars; i++) {
            for (int j = i; j < numVars; j++) {
                if (roundoff(matrixRow[i].elementsArr[j]) == 1) {
                    this.solArr[j] = roundoff(matrixRow[i].elementsArr[numVars]);
                }
            }
        }
    }

    public void printMatrix() {
        for (int i = 0; i < numEquations; i++) {
            System.out.println(Arrays.toString(matrixRow[i].elementsArr));
        }
    }

    /**
     * To handle the corner cases of Gauss Jordan Elimination method
     * For e.g. If pivot element is 0
     * @param row
     * @param rowIdx
     * @return
     */
    private boolean doSplOps(Row row, int rowIdx) {
        boolean interchanged = false;

        // If pivot element is 0
        if(row.elementsArr[rowIdx] == 0) {

            // Search for potential non-zero pivots below the current(rowIdx) row but same column.
            // If found, swap the 2 rows
            for (int j = rowIdx + 1; j < numEquations; j++) {
                if (matrixRow[j].elementsArr[rowIdx] != 0) {
                    // Swaps "row" and "matrixRow[j]"
                    SwapRowCommand swapRowCommand = new SwapRowCommand(row, matrixRow[j]);
                    System.out.println("R" + rowIdx + " <---> " + "R" + j);
                    controller.setCommand(swapRowCommand);
                    controller.executeCommand();
                    interchanged = true;
                    break;
                }
            }

            // If no non-zero pivot element is found below the current row (same column),
            // search for potential non-zero pivots in the same row but in subsequent columns.
            // Swap the columns if potential pivot is found.
            // Don't forget to remember this swap of columns because at the end they have to be
            // swapped back to get the correct answer
            // Carried out only when no row is interchanged
            if (!interchanged) {
                for (int k = rowIdx + 1; k < numVars; k++) {
                    if (roundoff(matrixRow[rowIdx].elementsArr[k]) != 0) {
                        matrixRow[rowIdx].elementsArr[k] = roundoff(matrixRow[rowIdx].elementsArr[k]);
                        System.out.println("C" + rowIdx + " <---> " + "C" + k);
                        columnsSwap.push(new Integer[]{rowIdx, k});
                        for (int r = 0; r < numEquations; r++) {      //numVars = numEquations
                            // Swaps rowIdx and kth indices of matrixRow[r] row in the matrix
                            SwapColumnCommand swapColumnCommand = new SwapColumnCommand(matrixRow[r], rowIdx, k);
                            controller.setCommand(swapColumnCommand);
                            controller.executeCommand();
                        }
                        interchanged = true;

                        break;
                    }
                }
            }

            // If above 2 operations are skipped, start searching for potential pivot in
            // bottom right sub-matrix of the element under observation.
            if (!interchanged) {
                for (int i = rowIdx + 1; i < numEquations; i++) {
                    for (int j = rowIdx + 1; j < numVars; j++) {
                        if (roundoff(matrixRow[i].elementsArr[j]) != 0) {
                            matrixRow[i].elementsArr[j] = roundoff(matrixRow[i].elementsArr[j]);
                            SwapRowCommand swapRowCommand = new SwapRowCommand(row, matrixRow[i]);
                            System.out.println("R" + rowIdx + " <---> " + "R" + j);
                            controller.setCommand(swapRowCommand);
                            controller.executeCommand();

                            System.out.println("C" + rowIdx + " <---> " + "C" + j);
                            columnsSwap.push(new Integer[]{rowIdx, j});
                            for (int r = 0; r < numVars; r++) {
                                // Swaps rowIdx and kth indices of matrixRow[r] row in the matrix
                                SwapColumnCommand swapColumnCommand = new SwapColumnCommand(matrixRow[r], rowIdx, j);
                                controller.setCommand(swapColumnCommand);
                                controller.executeCommand();
                            }
                            interchanged = true;

                            return true;
                        }
                    }
                }
            }

            // If still no potential pivot is found, then only 2 options are left,
            // either No Solution or Infinitely many solutions
            if (!interchanged) {
                if (roundoff(row.elementsArr[numVars]) != 0) {
                    row.elementsArr[numVars] = roundoff(row.elementsArr[numVars]);
                    this.ans = "No solutions";
                } else {
                    for (int i = rowIdx + 1; i < numEquations; i++) {
                        int cnt = 0;
                        for (int c = 0; c < numVars; c++) {
                            if (matrixRow[i].elementsArr[c] == 0) {
                                cnt++;
                            }
                        }
                        if (cnt == numVars && matrixRow[i].elementsArr[numVars] != 0) {
                            this.ans = "No solutions";
                            return  false;
                        }
                    }
                    this.ans = "Infinitely many solutions";
                }
                return false;
            }
        }
        return interchanged;
    }

    /**
     * To reverse the column swapping that might have been done in doSplOps function call
     */
    private void reverseColSwap() {
        while (!this.columnsSwap.empty()) {
            Integer[] columnIdx = this.columnsSwap.pop();
            for (int r = 0; r < numEquations; r++) {      //numVars = numEquations
                // Swaps rowIdx and kth indices of matrixRow[r] row in the matrix
                SwapColumnCommand swapColumnCommand = new SwapColumnCommand(matrixRow[r],
                        columnIdx[0], columnIdx[1]);
                controller.setCommand(swapColumnCommand);
                controller.executeCommand();
            }
        }
    }

    /**
     * Rounds off the input value to 4 decimal places
     * @param val
     * @return
     */
    private double roundoff (double val) {
        return Math.round(val * 10000d) / 10000d;
    }
}
