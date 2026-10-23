import java.io.*;

public class AvgNums {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        int n;
        double sum = 0.0, avg, t;

        System.out.print("Скільки чисел ви будете вводити: ");
        str = br.readLine();
        try {
            n = Integer.parseInt(str);
        } catch(NumberFormatException exc) {
            System.out.println("Недопустимий формат.");
            return;
        }

        System.out.println("Введіть " + n + " чисел.");
        for(int i = 0; i < n; i++) {
            str = br.readLine();
            try {
                t = Double.parseDouble(str);
            } catch(NumberFormatException exc) {
                System.out.println("Недопустимий формат.");
                t = 0.0;
            }
            sum += t;
        }
        avg = sum / n;
        System.out.println("Середнє значення: " + avg);
    }
}
