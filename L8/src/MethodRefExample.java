import java.util.function.Predicate;

class StudentUtils {
    public static boolean isPassingGrade(int grade){
        return grade >= 60;
    }
}

interface IntPredicate {
    boolean test(int value);
}

interface StudentCreator {
    StudentRef create(String name);
}

class StudentRef {
    private String name;
    StudentRef(String name){
        this.name = name;
    }
    String getName(){
        return this.name;
    }
    boolean hasPassed(int grade) {
        return grade >= 60;
    }
}

public class MethodRefExample {
    public static void main(String[] args) {
        IntPredicate passingPredicate = StudentUtils::isPassingGrade;
        System.out.println("Статичний метод (85): " + passingPredicate.test(85));

        StudentRef john = new StudentRef("John");
        IntPredicate passCheck = john::hasPassed;
        System.out.println("Метод екземпляра (55): " + passCheck.test(55));

        StudentCreator sc = StudentRef::new;
        StudentRef s = sc.create("Ivan");
        System.out.println("Створено через посилання на конструктор: " + s.getName());

        Predicate<Integer> isEven = (n) -> (n % 2) == 0;
        System.out.println("\nЧи є число 4 парним? " + isEven.test(4));
        System.out.println("Чи є число 5 парним? " + isEven.test(5));
    }
}
