module lk.ijse.java.chat_application_inp {
    requires javafx.controls;
    requires javafx.fxml;


    opens lk.ijse.java.chat_application_inp to javafx.fxml;
    exports lk.ijse.java.chat_application_inp;
}