class Printer {
    private boolean isBusy = false;

    synchronized void printDocument(String docName) {
        while(isBusy){
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Очікування перерване");
            }
        }
        isBusy = true;
        System.out.println("Друк документу:" + docName);
        isBusy = false;
        notifyAll();
    }
}

class PrintThread extends Thread {
    Printer printer;
    String docName;

    PrintThread(Printer printer, String docName) {
        this.printer = printer;
        this.docName = docName;
    }

    public void run() {
        printer.printDocument(docName);
    }
}

public class WaitNotifyExample {
    public static void main(String[] args) {
        Printer printer = new Printer();
        new PrintThread(printer, "Звіт_за_місяць.pdf").start();
        new PrintThread(printer, "Презентація.pptx").start();
        new PrintThread(printer, "Курсова_робота.docx").start();
    }
}
