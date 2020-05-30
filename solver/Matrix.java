package solver;

import solver.commands.AddRowCommand;
import solver.commands.MultRowWithConstCommand;
import solver.commands.SwapColumnCommand;
import solver.commands.SwapRowCommand;
import solver.controller.Controller;

import java.util.Stack;

//Client class
public class Matrix {
    private Controller controller;
    public Row[] matrixRow;
    public static int numVars;
    public static int numEquations;
    private int currRowNum = 0;
    private Stack<Integer[]> columnsSwap;
    public ComplexNumber[] solArr;
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
        this.solArr = new ComplexNumber[numVars];
        this.ans = "";
        this.significantEq = 0;
    }

    public void addRowInMatrix (String[] elements) {
        matrixRow[currRowNum++].generateRow(elements);
        if (!matrixRow[currRowNum - 1].allZeroRow) {
            this.significantEq++;
        }
    }

    public void solveEquations () {

        //To make the elements of lower triangular matrix equal 0 and make the diagonal entries equal 1
        for (int i = 0; i < numEquations - 1; i++) {
            boolean interchanged = doSplOps(matrixRow[i], i);
            if (this.ans.equals("No solutions") || this.ans.equals("Infinitely many solutions")) {
                return;
            }
            for (int j = i + 1; j < numEquations; j++) {
                // Just to print the row operation carried out
                ComplexNumber constant = matrixRow[j].elementsArr[i].productWithConst(-1).divideByComplexNum(matrixRow[i].elementsArr[i]);
                if (!constant.checkIfZero()) {
                    System.out.println(constant + " * R" + i + " + R" + j + " --> " + "R" + j);

                    MultRowWithConstCommand multRowWithConstCommand = new MultRowWithConstCommand(matrixRow[i],
                            constant);
                    controller.setCommand(multRowWithConstCommand);
                    controller.executeCommand();
                    Row tempRow = multRowWithConstCommand.multRow;
                    AddRowCommand addRowCommand = new AddRowCommand(matrixRow[j], tempRow);
                    controller.setCommandAndRow(addRowCommand, tempRow);
                    controller.executeCommand();
                }
            }

            //To make the pivot element of current row = 1
            if (!matrixRow[i].elementsArr[i].equals(new ComplexNumber("1+0i"))) {
                System.out.println(("1 / " + matrixRow[i].elementsArr[i].toString()) +
                        " * R" + i + " --> " + "R" + i);
                ComplexNumber constant = (new ComplexNumber(1, 0, '+')).
                        divideByComplexNum(matrixRow[i].elementsArr[i]);
                MultRowWithConstCommand multRowWithConstCommand = new MultRowWithConstCommand(matrixRow[i],
                        constant);
                controller.setCommand(multRowWithConstCommand);
                controller.executeCommand();
                matrixRow[i] = multRowWithConstCommand.multRow;
            }

            //To make the pivot element of last row  = 1
            if (i == numVars - 2 && !matrixRow[i + 1].elementsArr[i + 1].equals(new ComplexNumber("1+0i"))) {
                interchanged = doSplOps(matrixRow[i + 1], i + 1);
                if (this.ans.equals("No solutions") || this.ans.equals("Infinitely many solutions")) {
                    return;
                }
                System.out.println(("1 / " + matrixRow[i + 1].elementsArr[i + 1].toString()) +
                        " * R" + (i + 1) + " --> " + "R" + (i + 1));
                ComplexNumber constant = (new ComplexNumber(1, 0, '+')).
                        divideByComplexNum(matrixRow[numVars - 1].elementsArr[numVars - 1]);
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
                    if (roundoff(matrixRow[i].elementsArr[j]).checkIfZero()) {
                        cnt++;
                    }
                }
                if (cnt == numVars && !roundoff(matrixRow[i].elementsArr[numVars]).checkIfZero()) {
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
                if (!roundoff(matrixRow[i].elementsArr[numVars]).checkIfZero()) {
                    matrixRow[i].elementsArr[numVars] = roundoff(matrixRow[i].elementsArr[numVars]);
                    this.ans = "No solutions";
                    return;
                }
            }

            //To make the elements of upper triangular matrix equal 0
            for (int i = numVars - 1; i > 0; i--) {
                for (int j = i - 1; j >= 0; j--) {
//                    double constant = -1 * matrixRow[j].elementsArr[i] / matrixRow[i].elementsArr[i];
                    ComplexNumber constant = matrixRow[j].elementsArr[i].productWithConst(-1).
                            divideByComplexNum(matrixRow[i].elementsArr[i]);
                    if (!constant.checkIfZero()) {
                        System.out.println(constant.toString() + " * R" + i + " + R" + j + " --> " + "R" + j);
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
                if (roundoff(matrixRow[i].elementsArr[j]).equals(new ComplexNumber("1+0i"))) {
                    this.solArr[j] = this.roundoff(matrixRow[i].elementsArr[numVars]);
                }
            }
        }
    }

    private boolean doSplOps(Row row, int rowIdx) {
        boolean interchanged = false;
        if(row.elementsArr[rowIdx].checkIfZero()) {

            // For Swapping the rows
            for (int j = rowIdx + 1; j < numEquations; j++) {
                if (!matrixRow[j].elementsArr[rowIdx].checkIfZero()) {
                    // Swaps "row" and "matrixRow[j]"
                    SwapRowCommand swapRowCommand = new SwapRowCommand(row, matrixRow[j]);
                    System.out.println("R" + rowIdx + " <---> " + "R" + j);
                    controller.setCommand(swapRowCommand);
                    controller.executeCommand();
                    interchanged = true;
                    break;
                }
            }

            //For swapping the columns. Carried out only when no row is interchanged
            if (!interchanged) {
                for (int k = rowIdx + 1; k < numVars; k++) {
                    if (!roundoff(matrixRow[rowIdx].elementsArr[k]).checkIfZero()) {
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

            if (!interchanged) {
                for (int i = rowIdx + 1; i < numEquations; i++) {
                    for (int j = rowIdx + 1; j < numVars; j++) {
                        if (!roundoff(matrixRow[i].elementsArr[j]).checkIfZero()) {
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

            if (!interchanged) {
                if (!roundoff(row.elementsArr[numVars]).checkIfZero()) {
                    row.elementsArr[numVars] = roundoff(row.elementsArr[numVars]);
                    this.ans = "No solutions";
                } else {
                    for (int i = rowIdx + 1; i < numEquations; i++) {
                        int cnt = 0;
                        for (int c = 0; c < numVars; c++) {
                            if (matrixRow[i].elementsArr[c].checkIfZero()) {
                                cnt++;
                            }
                        }
                        if (cnt == numVars && !matrixRow[i].elementsArr[numVars].checkIfZero()) {
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

    private ComplexNumber roundoff (ComplexNumber val) {
        val.setRealPart(Math.round(val.getRealPart() * 10000d) / 10000d);
        val.setImaginaryPart(Math.round(val.getImaginaryPart() * 10000d) / 10000d);
        return val;
    }
}
