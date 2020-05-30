package solver;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputFile = args[0];
        String outputFile = args[1];
        File inFile = new File(inputFile);
        File outFile = new File(outputFile);
        Scanner scanner = new Scanner(inFile);
        int numOfVariables = scanner.nextInt();
        int numOfEquations = scanner.nextInt();
        String tmpStr = scanner.nextLine();

        Matrix m = new Matrix(numOfVariables, numOfEquations);
        String[] tmpRowArr;
        for (int i = 0; i < numOfEquations; i++) {
            tmpRowArr = scanner.nextLine().split(" ", 0);
            m.addRowInMatrix(tmpRowArr);
        }
        scanner.close();

        m.solveEquations();

        PrintWriter writer = new PrintWriter(outFile);
        if (m.ans.equals("No solutions") || m.ans.equals("Infinitely many solutions")) {
            System.out.println(m.ans);
            writer.println(m.ans);
        } else {
            m.printSolution();
            System.out.println("The Solution is: ( ");
            for (int i = 0; i < numOfVariables; i++) {
                System.out.println("                  " + m.solArr[i]);
                writer.println(m.solArr[i]);
            }
            System.out.print("                 )");
        }
        writer.close();
    }
}
