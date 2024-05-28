module game.casegame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens game.casegame to javafx.fxml;
    exports game.casegame;
}