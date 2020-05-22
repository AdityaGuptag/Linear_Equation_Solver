package solver;

import java.util.Arrays;

//Receiver class
public class Row {
    public static int numVars;
    protected ComplexNumber[] elementsArr;
    protected boolean allZeroRow;

    public Row (int numVars) {
        this.numVars = numVars;
        this.elementsArr = new ComplexNumber[this.numVars + 1];
        this.allZeroRow = true;
    }

    public void generateRow (String[] elements) {
        int cnt = 0;
        for (int i = 0; i < numVars + 1; i++) {
            this.elementsArr[i] = new ComplexNumber(elements[i]);
            if (this.elementsArr[i].checkIfZero()) {
                cnt++;
            }
        }
        if (cnt != numVars + 1) {
            this.allZeroRow = false;
        }
    }

    public Row prodWithConst (double constant) {
        ComplexNumber[] tempArr = Arrays.copyOf(elementsArr, numVars + 1);
        Row tempRow = new Row(numVars);
        String test = "";
        for (int i = 0 ; i < numVars + 1; i++) {
            tempArr[i].setRealPart(tempArr[i].getRealPart() * constant);
            tempArr[i].setImaginaryPart(tempArr[i].getImaginaryPart() * constant);
            test = test + tempArr[i].toString() + "^";
        }
        String[] strArr = test.split("^", 0);
        tempRow.generateRow(strArr);
        return tempRow;
    }

    public void sumWithRow(Row other) {
        for (int i = 0; i < numVars + 1; i++) {
            this.elementsArr[i].setRealPart(this.elementsArr[i].getRealPart() + other.elementsArr[i].getRealPart());
            this.elementsArr[i].setImaginaryPart(this.elementsArr[i].getImaginaryPart() + other.elementsArr[i].getImaginaryPart());
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
        ComplexNumber temp = this.elementsArr[col1];
        this.elementsArr[col1] = this.elementsArr[col2];
        this.elementsArr[col2] = temp;
    }

}
