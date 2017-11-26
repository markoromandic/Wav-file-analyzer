package view;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.packenius.library.xspdf.XSPDF;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.ResultDFT;
import static_variables.StaticVariables;
public class WindowCharts {
	
	private VBox root, boxForScroll;
	private Stage stageHistogram;
	private ArrayList<Node> rootItems;
	private Label selectFunction, timeFrom, timeTo, lengthOfFile;
	private TextField textFromMs, textToMs;
	private Button btnZoom;
	private ComboBox<String> combo;
	private NumberAxis xAxis, xAxisS, yAxisS;
	private CategoryAxis yAxis;
	private BarChart<String,Number> bc;
	private ScatterChart<Number,Number> sc;
	private ScrollPane scrollPaneMain, scrollPane, scrollPaneS;
	private XYChart.Series series1, series2;
	private ComboBox<String> comboPowerOfTwo;
	
	

	public WindowCharts() {
		this.stageHistogram = new Stage();
		rootItems = new ArrayList<>();
        root = new VBox();
        boxForScroll = new VBox();
		lengthOfFile = new Label();
		timeFrom = new Label("Time from(ms):");
		timeTo = new Label("Time to(ms):");
		selectFunction = new Label("Select function");
		textFromMs = new TextField();
		textToMs = new TextField();
		btnZoom = new Button("Draw!");
        combo = new ComboBox<>();
        comboPowerOfTwo = new ComboBox<>();
        rootItems.add(selectFunction);
        rootItems.add(combo);
        rootItems.add(comboPowerOfTwo);
		rootItems.add(lengthOfFile);
		rootItems.add(timeFrom);
		rootItems.add(textFromMs);
		rootItems.add(timeTo);
		rootItems.add(textToMs);
		rootItems.add(btnZoom);
		scrollPaneMain = new ScrollPane();
		
		xAxis = new NumberAxis();
		xAxisS = new NumberAxis();
        yAxis = new CategoryAxis();
		yAxisS = new NumberAxis();
        scrollPane = new ScrollPane();
        scrollPaneS = new ScrollPane();
        bc = new BarChart<String,Number>(yAxis, xAxis);
        sc = new ScatterChart<Number,Number>(yAxisS, xAxisS);
        scrollPane.setContent(bc);
        scrollPaneS.setContent(sc);
        rootItems.add(scrollPane);
        rootItems.add(scrollPaneS);
		
		core();
	}

	private void core(){
		stageHistogram.setTitle("Charts");
		combo.getItems().addAll(StaticVariables.functions);
		combo.getSelectionModel().select(0);
		comboPowerOfTwo.getItems().addAll(StaticVariables.powerOfTwo);
		comboPowerOfTwo.getSelectionModel().select(1);
		
        bc.setTitle("Histogram");
        sc.setTitle("Sonogram");
        scrollPane.setMaxSize(1250, 500);
        bc.setMinSize(1200, 400);
        scrollPaneS.setMaxSize(1250, 500);
        sc.setMinSize(1200, 400);
        xAxis.setLabel("Amplituda");
        yAxis.setLabel("Frekvencija");
        xAxis.setLabel("Frekvencija");
        yAxis.setLabel("Vreme");
        
		textFromMs.setMaxWidth(100);
		textToMs.setMaxWidth(100);
		
		scrollPaneMain.setContent(boxForScroll);
		boxForScroll.getChildren().addAll(rootItems);
		boxForScroll.setAlignment(Pos.CENTER);
		boxForScroll.setSpacing(10);
		boxForScroll.setMinSize(1300, 1000);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.getChildren().add(scrollPaneMain);
        
        stageHistogram.setScene(new Scene(root, 1300, 1000));
        stageHistogram.show();
	}

	public Stage getStageHistogram() {
		return stageHistogram;
	}

	public Label getLengthOfFile() {
		return lengthOfFile;
	}

	public TextField getTextFromMs() {
		return textFromMs;
	}

	public TextField getTextToMs() {
		return textToMs;
	}

	public Button getBtnZoom() {
		return btnZoom;
	}

	public ComboBox<String> getCombo() {
		return combo;
	}
	
	public ComboBox<String> getComboPowerOfTwo() {
		return comboPowerOfTwo;
	}

	public ScatterChart<Number, Number> getSc() {
		return sc;
	}

	public ScrollPane getScrollPaneS() {
		return scrollPaneS;
	}

	public BarChart<String, Number> getBc() {
		return bc;
	}

	public ScrollPane getScrollPane() {
		return scrollPane;
	}

	public void enterValuesFromModelHistogram(ArrayList<ResultDFT> results, int time, int sampleRate){
		double max = 0;
		double cur;
        series1.setName("opseg "+time);
        if(results == null) System.out.println("NULL SAM");
		for(int i=1; i<results.get(0).getReal().length/2; i++){
			cur = Math.abs(results.get(0).getReal()[i]);
			if(max<cur)max = cur;
		}
		double skalator = 100/max;
		for(int i=1; i<results.get(0).getReal().length/2; i++){
			series1.getData().add(new XYChart.Data((int)(i/((double)time/1000))+"Hz", Math.abs(results.get(0).getReal()[i]*skalator)));
		}
	}
	
	public void enterValuesFromModelSonogram(ArrayList<ResultDFT> results, double time, int sampleRate){
		double vremeXOsa = 0;
		for(int redniBrIzListe = 0; redniBrIzListe<results.size(); redniBrIzListe++){
			double max = 0;
			double cur;
			for(int i=1; i<results.get(redniBrIzListe).getReal().length/2; i++){
				cur = Math.abs(results.get(redniBrIzListe).getReal()[i]);
				if(max<cur)max = cur;
			}
			double skalator = 255/max;
			
			XYChart.Data dt;
			Circle c;
//			System.out.println("VREME X OSA: "+vremeXOsa);
//			System.out.println("TIME: "+time);
			for(int i=1; i<results.get(redniBrIzListe).getReal().length/2; i++){
				dt = new XYChart.Data(vremeXOsa, (i/((double)time/1000)));
				c = new Circle(1);
				c.setFill(Color.grayRgb(255-(int)Math.abs(results.get(redniBrIzListe).getReal()[i]*skalator)));
				dt.setNode(c);
				series2.getData().add(dt);
				
			}
			vremeXOsa+=time;
		}
		try {
			TimeUnit.SECONDS.sleep(5);
			doExport(bc, sc);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void emptyCharts(){
        series1 = new XYChart.Series();
        bc.getData().add(series1);
        series2 = new XYChart.Series();
        sc.getData().add(series2);
	}
	
	public static void doExport(Node histogramN, Node sonogramN) {
	    WritableImage histogram = histogramN.snapshot(new SnapshotParameters(), null);
	    WritableImage sonogram = sonogramN.snapshot(new SnapshotParameters(), null);
	    
	    File histogramF = new File("h.png");
	    File sonogramF = new File("s.png");
	    File pdfFile = new File("pdf.pdf");
	    
	    try {
	        ImageIO.write(SwingFXUtils.fromFXImage(histogram, null), "png", histogramF);
	        ImageIO.write(SwingFXUtils.fromFXImage(sonogram, null), "png", sonogramF);
	        
	        BufferedImage histogramToPdf = ImageIO.read(histogramF);
	        BufferedImage sonogramToPdf = ImageIO.read(sonogramF);
	        
	        histogramToPdf = convert24(histogramToPdf);
	        sonogramToPdf = convert24(sonogramToPdf);
	        
	        XSPDF.getInstance().setPageSize(595, 842).setImage(histogramToPdf, 0, 0, 400, 300, 0).setImage(sonogramToPdf, 0, 350, 400, 300, 0).createPdf(pdfFile);
	        
	        histogramF.delete();
	        sonogramF.delete();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	public static BufferedImage convert24(BufferedImage src) {
	    BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
	    ColorConvertOp cco = new ColorConvertOp(src.getColorModel().getColorSpace(), dest.getColorModel().getColorSpace(), null);
	    cco.filter(src, dest);
	    return dest;
	}
}
