class SyncTask {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

class SyncThread extends Thread {
    SyncTask task;

    SyncThread(SyncTask task) {
        this.task = task;
    }

    public void run() {
        for(int i = 0; i < 1000; i++) {
            task.increment();
        }
    }
}

public class SyncExample {
    public static void main(String[] args) throws InterruptedException {
        SyncTask task = new SyncTask();

        SyncThread t1 = new SyncThread(task);
        SyncThread t2 = new SyncThread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Значення лічильника: " + task.getCount());
    }
}
