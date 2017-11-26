package view;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import static_variables.StaticVariables;

public class WindowMain{
	
	private Button btnSelect;
	private ComboBox<String> combo;
	private VBox root;
	private Stage primaryStage;
	private ArrayList<Node> rootItems;
	
	public WindowMain(Stage primaryStage) {
		this.primaryStage = primaryStage;
		rootItems = new ArrayList<>();
        root = new VBox();
        combo = new ComboBox<>();
        rootItems.add(combo);
		btnSelect = new Button();
		rootItems.add(btnSelect);
        
		core();
		
	}
	
	private void core(){
		primaryStage.setTitle("Select file");
		
		btnSelect.setText("Select");
		btnSelect.setDisable(true);
		
        combo.getItems().addAll(StaticVariables.files);
        
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.getChildren().addAll(rootItems);
        
        primaryStage.setScene(new Scene(root, 200, 150));
        primaryStage.show();
	}

	public Button getBtnSelect() {
		return btnSelect;
	}

	public ComboBox<String> getCombo() {
		return combo;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	

	
	
	
}
