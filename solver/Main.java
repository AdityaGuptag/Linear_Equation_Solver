package solver;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        int cnt = 0;
        String[] tmpRowArr;
        for (int i = 0; i < numOfEquations; i++) {
            tmpRowArr = scanner.nextLine().split(" ", 0);
            System.out.println("Printing row - " + Arrays.toString(tmpRowArr));
            m.addRowInMatrix(tmpRowArr);
        }
        scanner.close();

//        m.solveEquations();
//
//        PrintWriter writer = new PrintWriter(outFile);
//        if (m.ans.equals("No solutions") || m.ans.equals("Infinitely many solutions")) {
//            System.out.println(m.ans);
//            writer.println(m.ans);
//        } else {
//            m.printSolution();
//            System.out.print("The Solution is: (");
//            for (int i = 0; i < numOfVariables; i++) {
//                System.out.print(m.solArr[i] + " ");
//                writer.println(m.solArr[i]);
//            }
//            System.out.print(")");
//        }
//        writer.close();
    }
}
