module com.u1tramarinet.imagemodifier {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.u1tramarinet.imagemodifier to javafx.fxml;
    exports com.u1tramarinet.imagemodifier;
}