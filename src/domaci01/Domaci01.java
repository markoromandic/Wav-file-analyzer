package domaci01;

import controller.Controller;
import javafx.stage.Stage;
import model.Model;
import view.View;
public class Domaci01 {

	public Domaci01(Stage primaryStage) {
		Model model = new Model();
		View view = new View(model, primaryStage);
		Controller controller = new Controller(model, view);
	}

}
