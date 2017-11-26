package controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.input.ScrollEvent;
import javafx.stage.WindowEvent;
import model.Model;
import view.View;

public class Controller {
	private Model model;
	private View view;
	public Controller(Model model, View view) {
		super();
		this.model = model;
		this.view = view;
		core();
	}
	
	private void core(){
		initializeWindowMain();
	}
	
	private void initializeWindowMain(){
		view.initializeWindowMain();
		view.getWindowMain().getBtnSelect().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
        		startLoad(view.getWindowMain().getCombo().getSelectionModel().getSelectedItem());
            }
        });
		view.getWindowMain().getCombo().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> ov, Object t, Object t1) {
            	view.getWindowMain().getBtnSelect().setDisable(false);
            }
        });
	}
	
	private void startLoad(String selectedName){
    	System.out.println(view.getWindowMain().getCombo().getSelectionModel().getSelectedItem());
    	view.getWindowMain().getPrimaryStage().hide();
		model.readFile(selectedName);
		view.initializeFromFile();
		view.getWindowCharts().getLengthOfFile().setText("File duration in seconds: "+model.getLengthOfFile());
		view.getWindowCharts().getStageHistogram().setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
				
			}
		});
		view.getWindowCharts().getBtnZoom().setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				int from, to;
				if(view.getWindowCharts().getTextFromMs().getText().equals("")) from = -1;
				else from = Integer.parseInt(view.getWindowCharts().getTextFromMs().getText());
				if(view.getWindowCharts().getTextToMs().getText().equals("")) to = -1;
				else to = Integer.parseInt(view.getWindowCharts().getTextToMs().getText());
				model.setPowerOfTwo(Integer.parseInt(view.getWindowCharts().getComboPowerOfTwo().getSelectionModel().getSelectedItem()));
				model.readingHistogram(from, to, view.getWindowCharts().getCombo().getSelectionModel().getSelectedItem());
				view.drawHistogram();
				model.readingSonogram(from, to, view.getWindowCharts().getCombo().getSelectionModel().getSelectedItem());
				view.drawSonogram();
			}
		});
		view.getWindowCharts().getScrollPane().setOnScroll(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				ScrollEvent scrollEvent = (ScrollEvent) event;
				if(scrollEvent.getDeltaY()>0)
					view.getWindowCharts().getBc().setMinWidth(view.getWindowCharts().getBc().getWidth()+50);
				else if(scrollEvent.getDeltaY()<0)
					view.getWindowCharts().getBc().setMinWidth(view.getWindowCharts().getBc().getWidth()-50);
			}
		});
		view.getWindowCharts().getScrollPaneS().setOnScroll(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				ScrollEvent scrollEvent = (ScrollEvent) event;
				if(scrollEvent.getDeltaY()>0)
					view.getWindowCharts().getSc().setMinWidth(view.getWindowCharts().getSc().getWidth()+50);
				else if(scrollEvent.getDeltaY()<0)
					view.getWindowCharts().getSc().setMinWidth(view.getWindowCharts().getSc().getWidth()-50);
			}
		});
	}
	
}
