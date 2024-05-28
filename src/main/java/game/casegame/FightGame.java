package game.casegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FightGame extends Application {

    private Player player;
    private Player opponent;
    private Label playerHpLabel;
    private Label opponentHpLabel;
    private Random random = new Random();
    private Map<String, String> actionCounterMap;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Fight Game");

        // Layout setup
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Player selection UI
        Label selectPlayerLabel = new Label("Select Player:");
        grid.add(selectPlayerLabel, 0, 0);
        TextField playerNameInput = new TextField();
        grid.add(playerNameInput, 1, 0);
        Button selectPlayerButton = new Button("Select Player");
        grid.add(selectPlayerButton, 2, 0);

        // HP labels
        playerHpLabel = new Label("Player HP: 100");
        grid.add(playerHpLabel, 0, 1);
        opponentHpLabel = new Label("Opponent HP: 100");
        grid.add(opponentHpLabel, 2, 1);

        // Action buttons
        Label actionsLabel = new Label("Actions:");
        grid.add(actionsLabel, 0, 2);

        Button boxButton = new Button("Box");
        grid.add(boxButton, 0, 3);
        Button kickButton = new Button("Kick");
        grid.add(kickButton, 1, 3);
        Button shootButton = new Button("Shoot");
        grid.add(shootButton, 2, 3);
        Button bendButton = new Button("Bend");
        grid.add(bendButton, 0, 4);
        Button jumpButton = new Button("Jump");
        grid.add(jumpButton, 1, 4);
        Button runButton = new Button("Run");
        grid.add(runButton, 2, 4);

        // Action images
        ImageView playerImageView = new ImageView();
        grid.add(playerImageView, 0, 5);
        ImageView opponentImageView = new ImageView();
        grid.add(opponentImageView, 2, 5);

        // Event handlers
        selectPlayerButton.setOnAction(e -> {
            String playerName = playerNameInput.getText();
            if (!playerName.isEmpty()) {
                player = new Player(playerName);
                opponent = new Player("Opponent");
                updateHpLabels();
                showAlert("Player Selected", "Player " + player.getName() + " selected.");
            }
        });

        boxButton.setOnAction(e -> performAction("box", "attack", playerImageView, opponentImageView));
        kickButton.setOnAction(e -> performAction("kick", "attack", playerImageView, opponentImageView));
        shootButton.setOnAction(e -> performAction("shoot", "attack", playerImageView, opponentImageView));
        bendButton.setOnAction(e -> performAction("bend", "defend", playerImageView, opponentImageView));
        jumpButton.setOnAction(e -> performAction("jump", "defend", playerImageView, opponentImageView));
        runButton.setOnAction(e -> performAction("run", "defend", playerImageView, opponentImageView));

        // Action counter map
        actionCounterMap = new HashMap<>();
        actionCounterMap.put("box", "bend");
        actionCounterMap.put("kick", "jump");
        actionCounterMap.put("shoot", "run");

        // Scene setup
        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void performAction(String actionName, String actionCategory, ImageView playerImageView, ImageView opponentImageView) {
        if (player == null || opponent == null) {
            return;
        }

        // Perform player's action
        player.performAction(actionName, opponent);
        playerImageView.setImage(new Image(getClass().getResourceAsStream("/" + actionCategory + "/" + actionName + ".png")));

        // Animate the action
        animateAction(playerImageView);

        // Opponent's response based on player's action
        String opponentAction = selectOpponentAction(actionCategory.equals("attack") ? "defend" : "attack");
        opponent.performAction(opponentAction, player);
        opponentImageView.setImage(new Image(getClass().getResourceAsStream("/" + (actionCategory.equals("attack") ? "defend" : "attack") + "/" + opponentAction + ".png")));

        // Animate the opponent's action
        animateAction(opponentImageView);

        // Check if actions match and adjust HP accordingly
        if (actionCategory.equals("attack")) {
            if (actionCounterMap.get(actionName).equals(opponentAction)) {
                // Successful defense, no HP change
                showAlert("Defend Successful", "Opponent successfully defended the attack!");
            } else {
                // Failed defense, reduce opponent HP
                opponent.reduceHp(10);
                showAlert("Attack Successful", "Opponent failed to defend the attack!");
            }
        }

        updateHpLabels();

        // Check for winner
        checkForWinner();
    }

    private String selectOpponentAction(String category) {
        String[] attackActions = {"box", "kick", "shoot"};
        String[] defendActions = {"bend", "jump", "run"};

        return category.equals("attack") ? attackActions[random.nextInt(attackActions.length)] : defendActions[random.nextInt(defendActions.length)];
    }

    private void animateAction(ImageView imageView) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> imageView.setVisible(true)),
                new KeyFrame(Duration.seconds(0.5), e -> imageView.setVisible(false))
        );
        timeline.setCycleCount(2);
        timeline.play();
    }

    private void updateHpLabels() {
        playerHpLabel.setText("Player HP: " + player.getHp());
        opponentHpLabel.setText("Opponent HP: " + opponent.getHp());
    }

    private void checkForWinner() {
        if (player.getHp() <= 0) {
            showAlert("Game Over", "Opponent wins!");
        } else if (opponent.getHp() <= 0) {
            showAlert("Game Over", "Player wins!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
