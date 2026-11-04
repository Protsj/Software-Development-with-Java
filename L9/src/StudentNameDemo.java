import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentNameDemo implements ActionListener {
    JTextField nameField;
    JLabel label;

    public StudentNameDemo() {
        JFrame frame = new JFrame("Введіть ім'я студента");
        frame.setLayout(new FlowLayout());
        frame.setSize(300, 120);

        nameField = new JTextField(10);
        nameField.addActionListener(this);

        JButton reverseButton = new JButton("Перевернути");
        reverseButton.addActionListener(this);

        label = new JLabel("Введіть ім'я студента: ");

        frame.add(label);
        frame.add(nameField);
        frame.add(reverseButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String studentName = nameField.getText();
        String reversedName = new StringBuilder(studentName).reverse().toString();
        nameField.setText(reversedName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentNameDemo::new);
    }
}