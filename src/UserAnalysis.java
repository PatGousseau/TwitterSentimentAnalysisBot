
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.Group;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.layout.BorderPane;



public class UserAnalysis extends Application {
	
	   private static int numPos = 0; //number of positive tweets
	   private static int numNeg = 0; //number of negative tweets
	   private static int numVPos = 0; //number of very positive tweets
	   private static int numVNeg = 0; //number of very negative tweets
	   private static int neutral = 0; //number of neutral tweets
	   
	   
//	public UserAnalysis(int numPos,int numNeg,int numVPos, int numVNeg,int neutral) {
//		this.numPos = numPos;
//		this.numNeg = numNeg;
//		this.numVPos = numVPos;
//		this.numVNeg = numVNeg;
//		this.neutral = neutral;
//	}

	 @Override public void start(Stage stage) {
	        Scene scene = new Scene(new Group());
	        stage.setTitle("Sentiment Analysis");
	        stage.setWidth(500);
	        stage.setHeight(500);
//	        BorderPane root = new BorderPane();
//	        Scene theScene = new Scene(root,500,500);
//	        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//	        stage.setScene(theScene);
	       // stage.show();
	        
	        

	        ObservableList<PieChart.Data> pieChartData =
	                FXCollections.observableArrayList(
	                new PieChart.Data("Positive", 4),
	                new PieChart.Data("Negative", 7),
	                new PieChart.Data("Very Positive", 3),
	                new PieChart.Data("Very Negative", 2),
	                new PieChart.Data("Neutral", 1));
	        final PieChart chart = new PieChart(pieChartData);
	       chart.setTitle("Sentiment Analysis");

	        ((Group) scene.getRoot()).getChildren().add(chart);
	        stage.setScene(scene);
	        stage.show();
	    }
	 public static void main(String args[]){   
		   launch(args);      
		}   
}
