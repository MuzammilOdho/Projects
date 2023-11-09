import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame implements ActionListener, KeyListener {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private int port = 7777;
    private String serverIP;

    private JPanel sidePanel,clientPanel;
    private JButton toggleButton, sendButton;

    private JTextField messageField;
    private JTextArea textArea;
    private boolean isSidePanelVisible = true;
    private String clientName;

    Client(String serverIP, String name) {
        initializeGUI();
        this.clientName = name;
        this.serverIP = serverIP;
        connect();
    }

    public void connect() {
        try {

            socket = new Socket(serverIP, port);
            textArea.setText("\t\t  Connected " + serverIP + "\n");
            textArea.append("\t\t  Enter '/exit' to disconnect from server\n");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("\t\t  <" + clientName + "> joined the chat ");

            reader();

        } catch (IOException e) {

         int opt = JOptionPane.showConfirmDialog(this,"Connecting Failed " + serverIP + "\n Retry Connecting",null, JOptionPane.YES_NO_OPTION);
            if ((opt == 0)) {
                connect();
            } else {
                System.exit(0);
            }
        }
    }

    public void reader() {

        Runnable r1 = () -> {

            try {
                while (!socket.isClosed()) {
                    String message = reader.readLine();
                    if (message.equals("7777/close")) {
                        JOptionPane.showMessageDialog(this, "Server Disconnected!!");
                        closeClient(0);
                        return;
                    } else if (message.startsWith("7777Client ,")) {

                        updateSidePanel(message);

                    } else if (message.startsWith("<") && message.endsWith("> left the chat ")) {

                        removeUserFromSidePanel(message);

                    } else {
                        textArea.append(message + "\n");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        };
        Thread r = new Thread(r1);
        r.start();
    }

    public void initializeGUI() {
        this.setTitle("Chat Application ~ Client");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        sendButton = new JButton("Send");
        sendButton.setBackground(Color.BLUE);
        sendButton.setForeground(Color.WHITE);
        sendButton.addActionListener(this);

        messageField = new JTextField(20);
        messageField.addKeyListener(this);

        JLabel connectedUsersLabel = new JLabel("Connected Users");


        clientPanel = new JPanel(new GridLayout(10,0,2,2));
        sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(Color.LIGHT_GRAY);
        sidePanel.add(connectedUsersLabel, BorderLayout.NORTH);
        sidePanel.add(clientPanel,BorderLayout.CENTER);


        toggleButton = new JButton("≪");
        toggleButton.addActionListener(this);


        JPanel input = new JPanel(new BorderLayout());
        input.add(new JLabel("Message: "), BorderLayout.WEST);
        input.add(messageField, BorderLayout.CENTER);
        input.add(sendButton, BorderLayout.EAST);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.add(toggleButton, BorderLayout.WEST);
        inputPanel.add(input, BorderLayout.CENTER);

        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.add(scrollPane, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);


        this.setLayout(new BorderLayout(5, 5));
        this.add(sidePanel, BorderLayout.WEST);
        this.add(chatPanel, BorderLayout.CENTER);


        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
          if(!messageField.getText().isEmpty()){
              writeMessage();
          }
        }
        if (e.getSource() == toggleButton) {
            toggleSidePanel();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 10) {
            if (!messageField.getText().isEmpty()) {
                writeMessage();
            }
        }
    }

    private void writeMessage() {
        String msg = messageField.getText();
        if (msg.equals("/exit")) {
            if (!socket.isClosed()) {

                int option = JOptionPane.showConfirmDialog(this, "Disconnect from Server ", "Confirmation", JOptionPane.YES_NO_OPTION);
                closeClient(option);

            }

        } else {

            writer.println("<" + clientName + ">" + " " + msg);
            messageField.setText("");
            textArea.append("Me : " + msg + "\n");
        }
    }

    private void closeClient(int option) {
        if (option == 0) {
            try {
                writer.println("\t\t  <" + clientName + "> left the chat ");
                writer.println("/exit");
                reader.close();
                writer.close();
                socket.close();
                SwingUtilities.invokeLater(() -> System.exit(0));

            } catch (Exception a) {
                JOptionPane.showMessageDialog(this,"Error while closing connection " + a.getMessage());
            }
        } else {
            messageField.setText("");
        }
    }

    private void toggleSidePanel() {
        if (isSidePanelVisible) {
            sidePanel.setVisible(false);
            toggleButton.setText("≫");
        } else {
            sidePanel.setVisible(true);
            toggleButton.setText("≪");
        }
        isSidePanelVisible = !isSidePanelVisible;
        revalidate();
    }
    private void updateSidePanel(String message) {
        String[] parts = message.split(", ");
        clientPanel.removeAll();
        for (int i = 1; i <parts.length ; i++) {

                JLabel label = new JLabel(parts[i]);
                clientPanel.add(label);

        }
        sidePanel.revalidate();
    }
    private void removeUserFromSidePanel(String message) {
        String username = message.substring(1 , message.indexOf('>'));
        Component[] components = sidePanel.getComponents();

        for (Component component : components) {
            if (component instanceof JLabel label) {
                if (label.getText().equals(username)) {
                    clientPanel.remove(label);
                    sidePanel.revalidate();
                    sidePanel.repaint();
                    break;
                }
            }
        }
    }

}
