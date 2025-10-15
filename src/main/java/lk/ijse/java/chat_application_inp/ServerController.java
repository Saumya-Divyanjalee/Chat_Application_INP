package lk.ijse.java.chat_application_inp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {

    public ImageView imageViewId;
    @FXML
    private Button btnsenId;

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtMessage;

    ServerSocket serverSocket;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String message ="";

    public void initialize() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(3000);
                txtArea.appendText("Server started\n");

                socket = serverSocket.accept();
                txtArea.appendText("Client connected\n");


                dataInputStream = new DataInputStream(socket.getInputStream());

                while (true) {
                    message=dataInputStream.readUTF();
                    if(message.equals("IMAGE")){
                        int length = dataInputStream.readInt();
                        byte[] imageBytes = new byte[length];
                        dataInputStream.readFully(imageBytes);
                        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                        Image image = new Image(bais);
                        imageViewId.setImage(image);


                    }
                    txtArea.appendText(message+"\n");

                    }
                } catch (IOException e) {
                 e.printStackTrace();
            }

        }).start();

    }

    @FXML
    void sendOnAction(ActionEvent event) throws IOException {

        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(txtMessage.getText());
        dataOutputStream.flush();
    }
}
