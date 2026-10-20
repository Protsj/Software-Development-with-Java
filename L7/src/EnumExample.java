enum Subject {
    MATHEMATICS(5), PHYSICS(4), LITERATURE(3), HISTORY(2);

    private int complexity;

    Subject(int complexity) {
        this.complexity = complexity;
    }

    public int getComplexity() {
        return complexity;
    }
}

public class EnumExample {
    public static void main(String[] args) {
        System.out.println("Усі предмети та їх складність:");
        for (Subject subject : Subject.values()) {
            System.out.println(subject + " має рівень складності: " + subject.getComplexity() +
                    ", порядковий номер: " + subject.ordinal());
        }

        System.out.println("\nПорівняння:");
        Subject subject1 = Subject.MATHEMATICS;
        Subject subject2 = Subject.PHYSICS;

        if(subject1.compareTo(subject2) < 0) {
            System.out.println(subject1 + " перед " + subject2);
        } else if (subject1.compareTo(subject2) > 0){
            System.out.println(subject1 + " після " + subject2);
        } else {
            System.out.println(subject1 + " дорівнює " + subject2);
        }
    }
}
