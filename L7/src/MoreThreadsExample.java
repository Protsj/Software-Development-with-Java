class MyThread implements Runnable {
    Thread thread;
    String threadName;

    MyThread(String name) {
        threadName = name;
        thread = new Thread(this, name);
    }

    public static MyThread createAndStart(String name) {
        MyThread myThread = new MyThread(name);
        myThread.thread.start();
        return myThread;
    }

    @Override
    public void run() {
        System.out.println("Поток " + threadName + " запущено.");
        try {
            for (int count = 0; count < 10; count++) {
                Thread.sleep(400);
                System.out.println("В потоці " + threadName + " значення count = " + count);
            }
        } catch (InterruptedException exc) {
            System.out.println(threadName + " перервано.");
        }
        System.out.println("Поток " + threadName + " завершено.");
    }
}

public class MoreThreadsExample {
    public static void main(String[] args) {
        System.out.println("Запуск потоків.");

        MyThread mt1 = MyThread.createAndStart("Child #1");
        MyThread mt2 = MyThread.createAndStart("Child #2");
        MyThread mt3 = MyThread.createAndStart("Child #3");

        try {
            mt1.thread.join();
            mt2.thread.join();
            mt3.thread.join();
        } catch (InterruptedException exc) {
            System.out.println("Головний потік перервано.");
        }
        System.out.println("Головний потік завершено.");
    }
}
