package pl.vistula.multithreads;

public class Priority implements Runnable {
    int count;
    Thread thread;
    static boolean stop = false;
    static String currentName;
    static int highPriorityFirstCount = 0; // Counter for high priority thread finishing first

    // Create a new thread
    Priority(String name) {
        thread = new Thread(this, name);
        count = 0;
        currentName = name;
    }

    public void run() {
        System.out.println(thread.getName() + " starts to operate");
        do {
            count++;
            if (currentName.compareTo(thread.getName()) != 0) {
                currentName = thread.getName();
                System.out.println(currentName + " is executed");
            }
        } while (stop == false && count < 10_000_000);
        stop = true;
        System.out.println("\n" + thread.getName() + " vanishes running");

        if (thread.getPriority() == Thread.MAX_PRIORITY) {
            synchronized (Priority.class) {
                highPriorityFirstCount++;
            }
        }
    }
}

class PriorityDemo {
    public static void main(String[] args) {
        int highPriorityFirstTotalCount = 0;

        for (int i = 0; i < 10; i++) {
            Priority mt1 = new Priority("High priority thread");
            Priority mt2 = new Priority("Low priority thread");
            Priority mt3 = new Priority("Thread #1 with normal priority");
            Priority mt4 = new Priority("Thread #2 with normal priority");
            Priority mt5 = new Priority("Thread #3 with normal priority");

            mt1.thread.setPriority(Thread.MAX_PRIORITY); // Set high priority thread to maximum priority
            mt2.thread.setPriority(Thread.MIN_PRIORITY); // Set low priority thread to minimum priority

            // Start threads
            mt1.thread.start();
            mt2.thread.start();
            mt3.thread.start();
            mt4.thread.start();
            mt5.thread.start();

            try {
                mt1.thread.join();
                mt2.thread.join();
                mt3.thread.join();
                mt4.thread.join();
                mt5.thread.join();
            } catch (InterruptedException e) {
                System.out.println("The main thread starts running");
            }

            System.out.println("High priority thread counted to " + mt1.count);
            System.out.println("Low priority thread counted to " + mt2.count);
            System.out.println("Thread #1 counted to " + mt3.count);
            System.out.println("Thread #2 counted to " + mt4.count);
            System.out.println("Thread #3 counted to " + mt5.count);

            highPriorityFirstTotalCount += Priority.highPriorityFirstCount;
            Priority.highPriorityFirstCount = 0;
        }

        System.out.println("\nNumber of times high priority thread finished first: " + highPriorityFirstTotalCount);
    }
}
