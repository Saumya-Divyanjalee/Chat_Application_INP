package lk.ijse.java.chat_application_inp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class ClientController {

    public Button btnImageId;
    public ImageView imageViewId;
    @FXML
    private Button sendId;

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtMessage;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    String message="";

    public void initialize() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 3000);
                dataInputStream = new DataInputStream(socket.getInputStream());

                while (true) {
                    message = dataInputStream.readUTF();
                    txtArea.appendText(message);
                    String type = dataInputStream.readUTF();

                    if (type.equals("IMAGE")) {
                        int length = dataInputStream.readInt();
                        byte[] imageBytes = new byte[length];
                        dataInputStream.readFully(imageBytes);

                        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                        Image image = new Image(bais);

                        Platform.runLater(() -> {
                            imageViewId.setImage(image);
                            txtArea.appendText("Received an image\n");
                        });

                        String textMessage = type;
                        Platform.runLater(() -> txtArea.appendText("Server: " + textMessage + "\n"));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void sendOnAction(ActionEvent event) throws IOException {
        dataOutputStream=new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(txtMessage.getText());
        dataOutputStream.flush();
    }


    public void sendImageOnAction(ActionEvent actionEvent) {
         FileChooser fileChooser = new FileChooser();
         File file = fileChooser.showOpenDialog(new Stage());
         if (file != null) {
             try{
                 byte[] imageBytes = Files.readAllBytes(file.toPath());
                 dataOutputStream = new DataOutputStream(socket.getOutputStream());
                 dataOutputStream.writeUTF("IMAGE");
                 dataOutputStream.writeInt(imageBytes.length);
                 dataOutputStream.write(imageBytes);
                 dataOutputStream.flush();

                 txtArea.appendText(file.getName()+"\n");
                 txtArea.appendText(file.getAbsolutePath()+"\n");
             } catch (IOException e) {
                 throw new RuntimeException(e);
             }
         }
    }
}
