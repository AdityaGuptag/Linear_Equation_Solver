package solver;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // This String stores the input file path
        String inputFile = args[0];

        // This String stores the output file path
        String outputFile = args[1];

        // File Handlers for input and output files
        File inFile = new File(inputFile);
        File outFile = new File(outputFile);

        Scanner scanner = new Scanner(inFile);
        int numOfVariables = scanner.nextInt();
        int numOfEquations = scanner.nextInt();
        double[] tmpRowArr = new double[numOfVariables + 1];

        // The linear equtions from input file are stored into Matrix m
        Matrix m = new Matrix(numOfVariables, numOfEquations);
        int cnt = 0;

        for (int i = 0; i < numOfEquations * (numOfVariables + 1); i++) {
            tmpRowArr[i % (numOfVariables + 1)] = scanner.nextDouble();
            cnt++;
            if (cnt == numOfVariables + 1) {
                cnt = 0;
                m.addRowInMatrix(tmpRowArr);
            }
        }
        scanner.close();

        // This function solves the linear equations
        m.solveEquations();

        // Here, solutions to the linear equations computed in previous step is stored in the output file
        PrintWriter writer = new PrintWriter(outFile);
        if (m.ans.equals("No solutions") || m.ans.equals("Infinitely many solutions")) {
            System.out.println(m.ans);
            writer.println(m.ans);
        } else {
            m.printSolution();
            System.out.print("The Solution is: (");
            for (int i = 0; i < numOfVariables; i++) {
                System.out.print(m.solArr[i] + " ");
                writer.println(m.solArr[i]);
            }
            System.out.print(")");
        }
        writer.close();
    }
}
