import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class Server extends JFrame implements ActionListener, KeyListener {
    private String serverName;
    private ServerSocket serverSocket;
    private ArrayList<Socket> clients ;
    private ArrayList<String> clientNameList;
    private JPanel sidePanel,clientPanel;
    private JButton toggleButton, sendButton;

    private JTextField messageField;
    private JTextArea textArea;
    private boolean isSidePanelVisible = false;

    Server(String name) {
        this.serverName = name;
        clients = new ArrayList<>();
        clientNameList = new ArrayList<>();
        initializeGUI();
        startServer();
    }
    public void startServer(){
        try {

            serverSocket = new ServerSocket(7777);
            serverSocket.setSoTimeout(1000);
            textArea.append("\t\t  <" + serverName + "> Created Server on IP : " + InetAddress.getLocalHost().getHostAddress() + "\n");
            textArea.append("\t\t  Enter '/close ' in new line to Close the server \n");
            textArea.append("\t\t  Enter '/save' in new line to save a backup copy of chat\n");

            while (!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    clients.add(clientSocket);
                    new ClientHandler(clientSocket).start();
                }catch (SocketTimeoutException e){
                    if(serverSocket.isClosed()){
                        break;
                    }
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,"Error creating Server " + e.getMessage(),null,JOptionPane.WARNING_MESSAGE);
        }

    }

     class ClientHandler extends Thread {
        private Socket clientSocket;
        private String clientName;

        ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String message=reader.readLine();
                clientName = message.substring(2 , message.indexOf('>'));
                addClient(clientName);
                textArea.append(message + "\n");
                broadCast(message,clientSocket);
                while (!(message = reader.readLine()).equals("/exit")) {
                    textArea.append(message + "\n");
                    broadCast(message, clientSocket);
                }
            } catch (IOException e) {
               JOptionPane.showMessageDialog(null,clientName + " connection lost!!");
               broadCast("\t\t  <"+clientName+"> left the chat\n",clientSocket);
            } finally {
                try {
                    clientSocket.close();
                    clients.remove(clientSocket);
                    clientNameList.remove(clientName);
                    removeClientFromSidePanel(clientName);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,"Error while closing client socket: " + e.getMessage(),null,JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }


    public void broadCast(String message, Socket sender) {

        for (Socket socket : clients) {
            try {
                if (socket != sender) {
                    PrintWriter clientWriter = new PrintWriter(socket.getOutputStream(), true);
                    clientWriter.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void initializeGUI() {
        this.setTitle("Chat Application");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and customize components
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

        clientPanel =new JPanel(new GridLayout(10,0,2,2));
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
            if (!messageField.getText().isEmpty()) {
                writeMessage();

            }
        }
        if (e.getSource() == toggleButton){
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
        if (msg.equals("/close")) {

            int option = JOptionPane.showConfirmDialog(this, "Close Server ", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == 0) {

                broadCast("7777" + msg, null);
                closeServer();
            }

        }else if (msg.startsWith("/save")) {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a file to save the chat");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)" ,"txt");
            fileChooser.setFileFilter(filter);

            int returnValue = fileChooser.showSaveDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".txt")) {
                    filePath += ".txt";
                }

                saveChatToFile(filePath);
            }

            messageField.setText("");
        }else {

            broadCast("<" + serverName + "(host)> " + msg, null);
            messageField.setText("");
            textArea.append("Me : " + msg + "\n");
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

    private void addClient(String name){
        JLabel label = new JLabel(name);
        clientNameList.add(name);
        clientPanel.add(label);
        updateClientsPanel();
        sidePanel.revalidate();
    }
    private void removeClientFromSidePanel(String clientNameToRemove) {
        clientNameList.remove(clientNameToRemove);
        Component[] components = clientPanel.getComponents();

        for (Component component : components) {
            if (component instanceof JLabel label) {
                if (label.getText().equals(clientNameToRemove)) {
                    clientPanel.remove(label);
                    sidePanel.revalidate();
                    sidePanel.repaint();
                    break;
                }
            }
        }
    }

    private void updateClientsPanel(){
        StringBuilder clients = new StringBuilder();
        clients.append("7777Client , ");
        for (String s : clientNameList){
            clients.append(s).append(" , ");
        }
        broadCast(clients.toString(),null);
    }
    public void closeServer(){
        for (Socket socket : clients){
            try {
                socket.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,"Error while closing client socket " + e.getMessage());
            }
        }
        clients.clear();

        try {

            serverSocket.close();
            System.exit(0);

        }catch (IOException e){

            JOptionPane.showMessageDialog(this,"Error while closing Server " + e.getMessage());
        }
    }
    private void saveChatToFile(String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            String chatText = textArea.getText();
            printWriter.print(chatText);
            JOptionPane.showMessageDialog(this, "Chat saved to file: " + filePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,"Error while saving chat to file: " + e.getMessage());
        }
    }


}



