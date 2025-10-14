package lk.ijse.java.chat_application_inp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientController {

    @FXML
    private Button sendId;

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtMessage;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public void initialize() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 3000);
                Platform.runLater(() -> txtArea.appendText("Connected to Server!\n"));

                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                String message;
                while (true) {
                    message = dataInputStream.readUTF();
                    String finalMessage = message;
                    Platform.runLater(() -> txtArea.appendText("Server: " + finalMessage + "\n"));
                }

            } catch (IOException e) {
                Platform.runLater(() -> txtArea.appendText("Connection error: " + e.getMessage() + "\n"));
            }
        }).start();
    }

    @FXML
    void sendOnAction(ActionEvent event) {
        try {
            String msg = txtMessage.getText().trim();
            if (!msg.isEmpty() && dataOutputStream != null) {
                dataOutputStream.writeUTF(msg);
                dataOutputStream.flush();
                txtArea.appendText("Client: " + msg + "\n");
                txtMessage.clear();
            }
        } catch (IOException e) {
            txtArea.appendText("Error sending message: " + e.getMessage() + "\n");
        }
    }
}
