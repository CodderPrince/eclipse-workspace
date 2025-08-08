package application;
public class SynchronizedSum {
    private static Integer sum = 0;

    public static synchronized void addToSum() {
        sum++;
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[1000];

        // Create and start 1,000 threads
        for (int i = 0; i < 1000; i++) {
            threads[i] = new Thread(() -> addToSum());
            threads[i].start();
        }

        // Wait for all threads to finish
        for (int i = 0; i < 1000; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("Synchronized sum: " + sum);
    }
}
