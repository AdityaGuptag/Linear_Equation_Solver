package solver;

import java.util.Arrays;

//Receiver class
public class Row {
    public static int numVars;
    protected double[] elementsArr;
    protected boolean allZeroRow;

    public Row (int numVars) {
        this.numVars = numVars;
        this.elementsArr = new double[this.numVars + 1];
        this.allZeroRow = true;
    }

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

    public Row prodWithConst (double constant) {
        double[] tempArr = Arrays.copyOf(elementsArr, numVars + 1);
        Row tempRow = new Row(numVars);
        for (int i = 0 ; i < numVars + 1; i++) {
            tempArr[i] = tempArr[i] * constant;
        }
        tempRow.generateRow(tempArr);
        return tempRow;
    }

    public void sumWithRow(Row other) {
        for (int i = 0; i < numVars + 1; i++) {
            this.elementsArr[i] = this.elementsArr[i] + other.elementsArr[i];
        }
    }

    public void swapRows(Row other) {
        Row temp = new Row(numVars);
        temp.elementsArr = other.elementsArr;
        other.elementsArr = this.elementsArr;
        this.elementsArr = temp.elementsArr;
    }

    //To be run on each row one-by-one
    public void swapColumns(int col1, int col2) {
        double temp = this.elementsArr[col1];
        this.elementsArr[col1] = this.elementsArr[col2];
        this.elementsArr[col2] = temp;
    }

}
