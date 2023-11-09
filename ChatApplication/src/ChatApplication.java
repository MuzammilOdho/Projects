import javax.swing.*;

public class ChatApplication {
    public static void main(String[] args) {
        String[] objects = {"Server", "Client"};
        String object = (String) JOptionPane.showInputDialog(null, "Login as ", "ChatApplication", JOptionPane.QUESTION_MESSAGE, null, objects, "Server");
        if(object != null) {

            if (object.equals("Server")) {
                String name = JOptionPane.showInputDialog("Your Name");
                new Server(name);
            } else {
                String serverIP = JOptionPane.showInputDialog("Enter Server IP Address");
                String name = JOptionPane.showInputDialog("Enter Your Name ");
                new Client(serverIP, name);
            }
        }
    }

}
