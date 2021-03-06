package app.gui;

import com.google.inject.Inject;
import com.google.inject.Provider;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Created by Karol on 2017-12-10.
 */

public class ContactController extends AbstractController {

    private Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    public ContactController(Provider<MainMenuController> mainMenuControllerProvider){
        this.mainMenuControllerProvider = mainMenuControllerProvider;
        this.scene = createScene(); }

    @Override
    public void show() {
        changeScene( this.scene);
    }

    @Override
    Scene createScene() {

        GridPane contactGrid = getAbstractGrid();

        VBox contactBox = new VBox(50);
        contactBox.setMinHeight(700);
        contactBox.setAlignment(Pos.CENTER);
        contactBox.setStyle("-fx-font: 40 Tahoma");
        contactGrid.add(contactBox, 0,0);

        contactBox.getChildren().addAll(
                getText("Authors", 70),
                getText("Email: gitanalyzer@gmail.com", 50),
                getText("Phone no: +48 789456123", 50),
                getButton("Back", 350, 55,
                        () -> mainMenuControllerProvider.get().show())
        );

        return new Scene(contactGrid, primaryStage.getWidth(), primaryStage.getHeight());
    }
}