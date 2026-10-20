public class AutoboxingExample {
    static void processStudentUD(Integer id){
        System.out.println("Оброблено студентський ID: " + id);
    }

    static int getBonusPoints(){
        return 5;
    }

    public static void main(String[] args) {
        Integer studentScore = 85;

        int finalScore = studentScore + 5;
        System.out.println("Остаточний бал студента: " + finalScore);

        processStudentUD(finalScore);

        Integer coursePoints = 90;
        int bonus = getBonusPoints();

        coursePoints += bonus;
        System.out.println("Загальна кількість балів курсу: " + coursePoints);
    }
}
