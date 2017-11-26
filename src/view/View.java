package view;

import javafx.stage.Stage;
import model.Model;

public class View {
	private Model model;
	private Stage primaryStage;
	private WindowMain windowMain;
	private WindowCharts windowCharts;

	public View(Model model, Stage primaryStage) {
		super();
		this.model = model;
		this.primaryStage = primaryStage;
		core();
	}
	
	private void core(){
		
	}
	
	public void initializeWindowMain(){
		windowMain = new WindowMain(primaryStage);
	}
	
	public void initializeFromFile(){
		windowCharts = new WindowCharts();
	}

	public WindowMain getWindowMain() {
		return windowMain;
	}

	public WindowCharts getWindowCharts() {
		return windowCharts;
	}
	
	public void drawHistogram(){
		windowCharts.emptyCharts();
		
		windowCharts.enterValuesFromModelHistogram(model.getResults(), model.getTimeSelectedMs(), model.getSampleRate());
	}
	
	public void drawSonogram(){
		double time = (((double)model.getPowerOfTwo()/model.getSampleRate())*1000);
		System.out.println("TIME: "+time);
		windowCharts.enterValuesFromModelSonogram(model.getResults(), time, model.getSampleRate());
	}
	
	
	
}
