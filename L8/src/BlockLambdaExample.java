interface NumericFunc{
    int func(int n);
}

interface MyFunc{
    boolean func(int n);
}

public class BlockLambdaExample {
    public static void main(String[] args) {
        NumericFunc findMinValue = (n) -> {
            int result = n;
            for (int i = 1; i <= n; i++) {
                if (i % 2 == 0){
                    result = i;
                    break;
                }
            }
            return result;
        };

        System.out.println("Найменше парне число до 5: " + findMinValue.func(5));
        System.out.println("Найменше парне число до 3: " + findMinValue.func(3));

        int maxStudents = 30;

        MyFunc checkGroupSize = (studentsInGroup) -> {
            return studentsInGroup <= maxStudents;
        };

        System.out.println("\nЧи поміститься група з 25 студентів? " + checkGroupSize.func(25));
        System.out.println("Чи поміститься група з 35 студентів? " + checkGroupSize.func(35));
    }
}
