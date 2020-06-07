package solver;

import java.util.Arrays;

/**
 * This class contains:
 * 1) Method to store the information read from input file in a structured way as rows of matrix
 * 2) Implementations of the commands which can be performed as row operations in
 *    Gauss Jordan Elimination method
 */
public class Row {
    public static int numVars;
    protected double[] elementsArr;  // Stores the coefficients of the equations and the constant on its RHS
    protected boolean allZeroRow;   // True if this Row is an all 0 row otherwise false

    public Row (int numVars) {
        this.numVars = numVars;
        this.elementsArr = new double[this.numVars + 1];
        this.allZeroRow = true;
    }

    /**
     * Stores the elements array into row object
     * @param elements
     */
    public void generateRow (double[] elements) {
        int cnt = 0;
        for (int i = 0; i < numVars + 1; i++) {
            if (elements[i] == 0) {
                cnt++;
            }
            this.elementsArr[i] = elements[i];
        }
        if (cnt != numVars + 1) {
            this.allZeroRow = false;
        }
    }

    /**
     * Command implementation if this row is needed to be multiplied with the passed constant arg
     * @param constant
     * @return
     */
    public Row prodWithConst (double constant) {
        double[] tempArr = Arrays.copyOf(elementsArr, numVars + 1);
        Row tempRow = new Row(numVars);
        for (int i = 0 ; i < numVars + 1; i++) {
            tempArr[i] = tempArr[i] * constant;
        }
        tempRow.generateRow(tempArr);
        return tempRow;
    }

    /**
     * Command implementation for adding this row into another row passed as argument
     * @param other
     */
    public void sumWithRow(Row other) {
        for (int i = 0; i < numVars + 1; i++) {
            this.elementsArr[i] = this.elementsArr[i] + other.elementsArr[i];
        }
    }

    /**
     * Command implementation for swapping this row with another row passed as arg
     * @param other
     */
    public void swapRows(Row other) {
        Row temp = new Row(numVars);
        temp.elementsArr = other.elementsArr;
        other.elementsArr = this.elementsArr;
        this.elementsArr = temp.elementsArr;
    }

    /**
     * Command implementation for swapping the columns of this row which are passed as arg to the function
     * (To be run on each row one-by-one)
     * @param col1
     * @param col2
     */
    public void swapColumns(int col1, int col2) {
        double temp = this.elementsArr[col1];
        this.elementsArr[col1] = this.elementsArr[col2];
        this.elementsArr[col2] = temp;
    }

}
