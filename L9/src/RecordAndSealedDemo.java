sealed class Course permits MathCourse, PhysicsCourse { }
final class MathCourse extends Course { }
final class PhysicsCourse extends Course { }

record StudentRecord(String name, int id, double averageGrade) {
    public StudentRecord {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ім'я не може бути пустим");
        }
        if(averageGrade < 0 || averageGrade > 100) {
            throw new IllegalArgumentException("Бал має бути між 0 і 100");
        }
    }
}

public class RecordAndSealedDemo {
    public static void main(String[] args) {
        StudentRecord student = new StudentRecord("Олексій", 12345, 90.5);
        System.out.println("Ім'я: " + student.name());
        System.out.println("Номер: " + student.id());
        System.out.println("Середній бал: " + student.averageGrade());

        Object obj = new StudentRecord("Іван", 54321, 95.0);
        if(obj instanceof StudentRecord s) {
            System.out.println("\nПеревірка instanceof пройдена. Студент: " + s.name());
        }

        Course myCourse = new MathCourse();
        System.out.println("Мій курс є об'єктом запечатаного класу: " + myCourse.getClass().getSimpleName());
    }
}