package application;
import java.util.concurrent.*;

public class ParallelMatrixMultiplication {

    public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) throws InterruptedException, ExecutionException {
        int rows = a.length;
        int cols = b[0].length;
        double[][] result = new double[rows][cols];

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<?>[] futures = new Future[rows];

        for (int i = 0; i < rows; i++) {
            final int row = i;
            futures[i] = executor.submit(() -> {
                for (int j = 0; j < cols; j++) {
                    for (int k = 0; k < b.length; k++) {
                        result[row][j] += a[row][k] * b[k][j];
                    }
                }
            });
        }

        for (Future<?> future : futures) {
            future.get(); // Wait for all tasks to complete
        }

        executor.shutdown();
        return result;
    }

    public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {
        int rows = a.length;
        int cols = b[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < b.length; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        return result;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int size = 2000;
        double[][] a = new double[size][size];
        double[][] b = new double[size][size];

        // Initialize matrices with random values
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[i][j] = Math.random();
                b[i][j] = Math.random();
            }
        }

        // Measure execution time for sequential multiplication
        long startTime = System.currentTimeMillis();
        sequentialMultiplyMatrix(a, b);
        long endTime = System.currentTimeMillis();
        System.out.println("Sequential multiplication time: " + (endTime - startTime) + " ms");

        // Measure execution time for parallel multiplication
        startTime = System.currentTimeMillis();
        parallelMultiplyMatrix(a, b);
        endTime = System.currentTimeMillis();
        System.out.println("Parallel multiplication time: " + (endTime - startTime) + " ms");
    }
}
