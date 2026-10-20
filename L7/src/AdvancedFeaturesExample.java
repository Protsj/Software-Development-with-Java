import static java.lang.Math.sqrt;
import static java.lang.Math.pow;

class Course {}
class OnlineCourse extends Course {}

@interface CourseInfo{
    String courseName();
    int year();
}

@CourseInfo(courseName = "Java Programming", year = 2024)
public class AdvancedFeaturesExample {
    @Deprecated
    public static void oldMethod(){
        System.out.println("Цей метод застарілий!");
    }

    public static void main(String[] args) {
        double a = 1, b = -3, c =  2;
        double discriminant = pow(b,2) - 4 * a * c;
        double x1 = (-b + sqrt(discriminant) / (2 * a));
        double x2 = (-b - sqrt(discriminant) / (2 * a));
        System.out.println("Корені рівняння: x1 = " + x1 + ", x2 = " + x2);

        Course course = new OnlineCourse();
        if(course instanceof OnlineCourse){
            System.out.println("Це онлайн-курс.");
        } else{
            System.out.println("Це не онлайн-курс.");
        }

        oldMethod();
    }
}
