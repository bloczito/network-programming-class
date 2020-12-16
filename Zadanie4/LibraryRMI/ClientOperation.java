import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.*;

public class ClientOperation {

    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {

//        look_up = (RMIInterface) Naming.lookup("//localhost/MyServer");
//        look_up = (RMIInterface) Naming.lookup("rmi://remotehost/MyServer");

        JFrame frame = new JFrame("Library");
        JPanel panel = new JPanel();

        JLabel nameLabel = new JLabel("Imie:");
        nameLabel.setBounds(50,20,100,30);

        JTextField nameTextField = new JTextField();
        nameTextField.setBounds(50, 50, 200, 30);

        JLabel surnameLabel = new JLabel("Nazwisko:");
        surnameLabel.setBounds(50,70,100,30);

        JTextField surnameField = new JTextField();
        surnameField.setBounds(50, 100, 200, 30);

        JLabel peselLabel = new JLabel("Pesel:");
        peselLabel.setBounds(50,120,100,30);

        JTextField peselField = new JTextField();
        peselField.setBounds(50, 150, 200, 30);

        JLabel passwordLabel = new JLabel("Hasło:");
        passwordLabel.setBounds(50,170,100,30);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(50, 200, 200, 30);


        JButton acceptButton = new JButton("Akceptuj");
        acceptButton.setBounds(50, 250, 200, 40);
        acceptButton.addActionListener((e) -> {
            String response = null;
            try {
                response = look_up.helloTo(nameTextField.getText(),surnameField.getText(),peselField.getText(), passwordField.getPassword());
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, response);
        });

        panel.setLayout(null);
        panel.add(nameTextField);
        panel.add(nameLabel);
        panel.add(surnameLabel);
        panel.add(surnameField);
        panel.add(peselField);
        panel.add(peselLabel);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(acceptButton);

        frame.add(panel);
        frame.setSize(330, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static RMIInterface look_up;
}
