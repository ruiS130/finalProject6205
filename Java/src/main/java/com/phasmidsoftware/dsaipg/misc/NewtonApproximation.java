/*
 * Copyright (c) 2017. Phasmid Software
 */

package com.phasmidsoftware.dsaipg.misc;

/**
 * The NewtonApproximation class demonstrates the Newton-Raphson method to find an approximate solution
 * for the equation cos(x) = x.
 * <p>
 * The Newton-Raphson method is an iterative numerical technique used to approximate the roots of a real-valued function.
 * In this case, the method solves the equation by iterating until the calculated y value (cos(x) - x) is close
 * enough to zero within a given tolerance.
 * <p>
 * The iterative formula used is:
 * x = x + y / (sin(x) + 1)
 * where:
 * y = cos(x) - x
 * <p>
 * The solution stops when the absolute value of y is less than the specified tolerance (1E-7).
 * <p>
 * This implementation demonstrates the use of a fixed iteration limit (200) to ensure termination,
 * and it displays the found solution if the convergence criterion is met within this limit.
 */
class NewtonApproximation {
    /**
     * The main method demonstrates the use of the Newton-Raphson approximation method to solve the equation cos(x) = x.
     * It iteratively refines the approximation of x until the difference (y = cos(x) - x) is within a specified tolerance (1E-7).
     * The iteration stops after a fixed number of attempts (200) or upon finding a solution that meets the precision criterion.
     *
     * @param args command-line arguments; not utilized in this implementation
     */
    public static void main(String[] args) {
        // Newton's Approximation to solve cos(x) = x
        double x = 1.0;
        int left = 200;
        for (; left > 0; left--) {
            final double y = Math.cos(x) - x;
            if (Math.abs(y) < 1E-7) {
                System.out.println("the solution to cos(x)=x is: " + x);
                System.exit(0);
            }
            x = x + y / (Math.sin(x) + 1);
        }
    }

    public static void MyNewtonApproximation() {
        // Newton's Approximation to solve x^3-2x^2+1 = 0
        double[] initialGuesses = {-1.0, 0.5 , 2.0};

        int left = 200;
        boolean has_solution = false;

        for (double guess : initialGuesses) {
            double x = guess;
            for (; left > 0; left--) {
                double fx = Math.pow(x, 3) - 2 * Math.pow(x, 2) + 1;
                double fxDerivative = 3 * Math.pow(x, 2) - 4 * x;

                if (Math.abs(fx) < 1E-7 )  {
                    System.out.println("one solution to x^3-2x^2+1 = 0 is: " + x);
                    has_solution = true;
                    break;
                }

                if(Math.abs(fxDerivative) < 1E-7) {
                    System.out.println("Derivative is too small to continue.");
                    break;
                }

                x = x - fx / fxDerivative;
            }
        }

        if (!has_solution) {
            System.out.println("No solution found.");
        }
    }
}