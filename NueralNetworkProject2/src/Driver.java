import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Driver extends Application{
	static double m = 0;
	static double b = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][][] data = Perceptron.andData;
		double[] weights = Perceptron.INITIAL_WEIGHTS;
		Driver driver = new Driver();
		Perceptron perceptron = new Perceptron();
		int epochNumber = 0;
		boolean errorFlag = true;
		double error = 0;
		double[] adjustedWeights = null;
		while(errorFlag){
			driver.printHeading(epochNumber++);
			errorFlag = false;
			error = 0;
			for(int x = 0; x  < data.length; x++){
				double weightedSum = perceptron.calculateWeightedSum(data[x][0],  weights);
				int result = perceptron.applyActivationFunction(weightedSum);
				error = data[x][1][0] - result;
				if(error != 0) errorFlag = true;
				adjustedWeights = perceptron.adjustWeights(data[x][0],  weights, error);
				driver.printVector(data[x], weights, result, error, weightedSum, adjustedWeights);
				weights = adjustedWeights;
			}
			m = -weights[1]/weights[0];
			b = Perceptron.THRESHOLD/weights[0];
			System.out.println("\ny = "+String.format("%.2f", m)+"x + "+String.format("%.2f", b));
		}
		//m = -weights[1]/weights[0];
		//b = Perceptron.THRESHOLD/weights[0];
		//System.out.println("\ny = "+String.format("%.2f", m)+"x "+String.format("%.2f", b));
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Neural Network Tutorial 2");
		XYChart.Series<Number, Number> series1 = new XYChart.Series<Number, Number>();
		series1.setName("zero");
		XYChart.Series<Number, Number> series2 = new XYChart.Series<Number, Number>();
		series2.setName("one");
		IntStream.range(0, Perceptron.andData.length).forEach(i -> {
			int x = Perceptron.andData[i][0][0], y = Perceptron.andData[i][0][1];
			if (Perceptron.andData[i][1][0] == 0) series1.getData().add(new XYChart.Data<Number, Number>(x, y));
			else series2.getData().add(new XYChart.Data<Number, Number>(x, y));
		});
		double x1 = 0, y1 = b, x2 = -(b/m), y2 = 0;
		String title = new String("y = "+String.format("%.2f",  m) + "x + " + String.format("%.2f", b)
		   + "  |  (0, " + String.format("%.2f",b) + ")  |  ("+ String.format("%.2f", (-b/m))+", 0)");
		NumberAxis xAxis = new NumberAxis(0, 4, 1);
		NumberAxis yAxis = new NumberAxis(0, 4, 1);
		ScatterChart<Number, Number> scatterChart = new ScatterChart<Number, Number>(xAxis, yAxis);
		scatterChart.setTitle(title);
		scatterChart.getData().add(series1);
		scatterChart.getData().add(series2);
		
		XYChart.Series<Number, Number> series3 = new XYChart.Series<Number, Number>();
		series3.getData().add(new XYChart.Data<Number, Number>(x1, y1));
		series3.getData().add(new XYChart.Data<Number, Number>(x2, y2));
		LineChart<Number, Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
		lineChart.getData().add(series3);
		lineChart.setOpacity(0.1);
		Pane pane = new Pane();
		pane.getChildren().addAll(scatterChart, lineChart);
		stage.setScene(new Scene(pane, 500, 400));
		stage.show();
	}

	public void printHeading(int epochNumber) {
		System.out.println("\n=============================================Epoch # "+epochNumber+"============================================");
		System.out.println("   w1  |  w2  | x1 | x2 | Target Result | Result | error | Weighted Sum | adjusted w1 | adjusted w2");
		System.out.println("---------------------------------------------------------------------------------------------------");
	}
	public void printVector(int[][] data, double[] weights, int result, double error, double weightedSum, double[] adjustedWeights){
		System.out.println("  "+String.format("%.2f",weights[0])+" | "+String.format("%.2f",weights[1])+" | "+data[0][0]+"  | "+data[0][1] +
						   "  |      "+data[1][0]+"        |   "+result+"    | "+error+"   |      "+String.format("%.2f",weightedSum) + 
						   "    |     "+ String.format("%.2f",adjustedWeights[0])+"    | "+String.format("%.2f",adjustedWeights[1]));
	}
}
