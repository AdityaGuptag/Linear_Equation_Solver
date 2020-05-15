package solver;

import solver.commands.AddRowCommand;
import solver.commands.MultRowWithConstCommand;
import solver.commands.SwapColumnCommand;
import solver.commands.SwapRowCommand;
import solver.controller.Controller;

import java.util.Arrays;
import java.util.Stack;

//Client class
public class Matrix {
    private Controller controller;
    public Row[] matrixRow;
    public static int numVars;
    public static int numEquations;
    private int currRowNum = 0;
    private Stack<Integer[]> columnsSwap;
    public double[] solArr;
    String ans;
    private int significantEq;

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

    public void addRowInMatrix (double[] elements) {
        matrixRow[currRowNum++].generateRow(elements);
        if (!matrixRow[currRowNum - 1].allZeroRow) {
            this.significantEq++;
        }
    }

    public void solveEquations () {

        //To make the elements of lower triangular matrix equal 0 and make the diagonal entries equal 1
        for (int i = 0; i < numEquations - 1; i++) {     //Here numVars = numEquations
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
                    controller.setCommandAndRow(addRowCommand, tempRow);
                    controller.executeCommand();

                    printMatrix();
                }
            }

            //To make the pivot element of current row = 1
            if (matrixRow[i].elementsArr[i] != 1) {
                System.out.println((1 / matrixRow[i].elementsArr[i]) +
                        " * R" + i + " --> " + "R" + i);
                MultRowWithConstCommand multRowWithConstCommand = new MultRowWithConstCommand(matrixRow[i],
                        1  / matrixRow[i].elementsArr[i]);
                controller.setCommand(multRowWithConstCommand);
                controller.executeCommand();
                matrixRow[i] = multRowWithConstCommand.multRow;

                printMatrix();
            }

            //To make the pivot element of last row  = 1
            if (i == numVars - 2 && matrixRow[i + 1].elementsArr[i + 1] != 1) {
                interchanged = doSplOps(matrixRow[i + 1], i + 1);
                if (this.ans.equals("No solutions") || this.ans.equals("Infinitely many solutions")) {
                    return;
                }
                System.out.println((1 / matrixRow[i + 1].elementsArr[i + 1]) +
                        " * R" + (i + 1) + " --> " + "R" + (i + 1));
                MultRowWithConstCommand multRowWithConstCommand = new MultRowWithConstCommand(matrixRow[numVars - 1],
                        1 / matrixRow[numVars - 1].elementsArr[numVars - 1]);
                controller.setCommand(multRowWithConstCommand);
                controller.executeCommand();
                matrixRow[numVars - 1] = multRowWithConstCommand.multRow;

                printMatrix();
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
            // To check the equations below the last equation in which pivot element was made =1
            for (int i = numVars; i < numEquations; i++) {
                if (roundoff(matrixRow[i].elementsArr[numVars]) != 0) {
                    matrixRow[i].elementsArr[numVars] = roundoff(matrixRow[i].elementsArr[numVars]);
                    this.ans = "No solutions";
                    return;
                }
            }

            //To make the elements of upper triangular matrix equal 0
            for (int i = numVars - 1; i > 0; i--) {
                for (int j = i - 1; j >= 0; j--) {
                    double constant = -1 * matrixRow[j].elementsArr[i] / matrixRow[i].elementsArr[i];
                    if (constant != 0.0) {
                        System.out.println(constant + " * R" + i + " + R" + j + " --> " + "R" + j);
                        matrixRow[j].sumWithRow(matrixRow[i].prodWithConst(constant));
                        printMatrix();
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
                    this.solArr[j] = matrixRow[i].elementsArr[numVars];
                }
            }
        }
    }

    public void printMatrix() {
        for (int i = 0; i < numEquations; i++) {
            System.out.println(Arrays.toString(matrixRow[i].elementsArr));
        }
    }

    private boolean doSplOps(Row row, int rowIdx) {
        boolean interchanged = false;
        if(row.elementsArr[rowIdx] == 0) {
            // For Swapping the rows
            for (int j = rowIdx + 1; j < numEquations; j++) {
                if (matrixRow[j].elementsArr[rowIdx] != 0) {
                    // Swaps "row" and "matrixRow[j]"
                    SwapRowCommand swapRowCommand = new SwapRowCommand(row, matrixRow[j]);
                    System.out.println("R" + rowIdx + " <---> " + "R" + j);
                    controller.setCommand(swapRowCommand);
                    controller.executeCommand();
                    interchanged = true;

                    printMatrix();

                    break;
                }
            }

            //For swapping the columns. Carried out only when no row is interchanged
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
                        printMatrix();
                        break;
                    }
                }
            }

            if (!interchanged) {
                for (int i = rowIdx + 1; i < numEquations; i++) {
                    for (int j = rowIdx + 1; j < numVars; j++) {
                        if (roundoff(matrixRow[i].elementsArr[j]) != 0) {
                            matrixRow[i].elementsArr[j] = roundoff(matrixRow[i].elementsArr[j]);
                            SwapRowCommand swapRowCommand = new SwapRowCommand(row, matrixRow[i]);
                            System.out.println("R" + rowIdx + " <---> " + "R" + j);
                            controller.setCommand(swapRowCommand);
                            controller.executeCommand();

                            printMatrix();

                            System.out.println("C" + rowIdx + " <---> " + "C" + j);
                            columnsSwap.push(new Integer[]{rowIdx, j});
                            for (int r = 0; r < numVars; r++) {
                                // Swaps rowIdx and kth indices of matrixRow[r] row in the matrix
                                SwapColumnCommand swapColumnCommand = new SwapColumnCommand(matrixRow[r], rowIdx, j);
                                controller.setCommand(swapColumnCommand);
                                controller.executeCommand();
                            }
                            interchanged = true;
                            printMatrix();
                            return true;
                        }
                    }
                }
            }

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

    private double roundoff (double val) {
        return Math.round(val * 10000d) / 10000d;
    }
}
