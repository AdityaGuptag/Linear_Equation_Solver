# Linear Equation Solver
Solves the Linear equations by Gauss Jordan Elimination method.
It can now solve even the equations with complex number coefficients!

The code requires 2 args to run.
First, the path of a file to read the equations and second, is the path of the file to store the output.

The first line of the input file is 2 space separated integers. Where first integer corresponds to the number of equations and second corresponds to the number of unknowns.
Following that, the file contains the space separated coefficient's values of each equation and the constant value, in a single line.
Similarly each equation is written in a new line.

The output file contains the values of the unknowns obtained after solving, each in a new line.
If the equations are inconsistent than, "No solutions" is stored in the output file or if it has infinite solutions than "Infinitely many solutions"
is stored.
