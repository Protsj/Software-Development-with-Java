import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Student{
    String name;
    int grade;

    Student(String name,int grade){
        this.name = name;
        this.grade = grade;
    }
    public int getGrade() {
        return grade;
    }
    public String getName(){
        return name;
    }
}

interface StudentScore{
    double getAverageGrade(int[] grades);
}

public class LambdaExample {
    public static void main(String[] args) {
        List<Student> students=new ArrayList<>();
        students.add(new Student("Ivan", 85));
        students.add(new Student("Olga", 92));
        students.add(new Student("Petro", 88));
        students.add(new Student("Maria", 95));

        List<Student> topStudents = students.stream()
                .filter(student -> student.getGrade() >= 90)
                .collect(Collectors.toList());

        System.out.println("Студенти з оцінками >= 90:");
        topStudents.forEach(student -> System.out.println(student.getName()));

        StudentScore studentScore = (grades) -> {
            int sum = 0;
            for (int grade : grades) {
                sum += grade;
            }
            return (double) sum / grades.length;
        };

        int[] myGrades = {85, 90, 95};
        System.out.println("\nСередній бал: " + studentScore.getAverageGrade(myGrades));
    }
}
