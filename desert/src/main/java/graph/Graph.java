import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class Graph {
    String name;
    LineChart lineChart;
    BarChart barChart;
    PieChart pieChart;
    StackPane pane;
    ArrayList<XYChart.Series> series;
    ArrayList<XYChart.Series> series1;
    ArrayList<XYChart.Series> series2;
    int numYears;



    public Graph(String graphName){
        name = graphName;
        series = new ArrayList<XYChart.Series>();
        series1 = new ArrayList<XYChart.Series>();
        series2 =  new ArrayList<XYChart.Series>();

        NumberAxis xAxis = new NumberAxis();
        CategoryAxis cxAxis = new CategoryAxis();
        cxAxis.setLabel("Year");
        cxAxis.setAnimated(true);

        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(true);
        xAxis.setLabel("Year");
        xAxis.setTickUnit(1d);

        pane = new StackPane();
        lineChart = new LineChart(xAxis,new NumberAxis());
        barChart = new BarChart(cxAxis,new NumberAxis());
        pieChart = new PieChart();
        barChart.setAnimated(true);
        lineChart.setAnimated(true);
        lineChart.setCursor(Cursor.CROSSHAIR);
        switchGraph("LineGraph");

    }
    public XYChart.Series getSeries(String name, ArrayList<XYChart.Series> list){
        for(XYChart.Series x:list){
            if(x.getName() == name){
                return x;
            }
        }
        XYChart.Series returnSeries = new XYChart.Series();
        list.add(returnSeries);
        barChart.getData().add(returnSeries);
        return returnSeries;
    }

    public void addSeries(String name, HashMap<Integer, Double> a){
        XYChart.Series series = new XYChart.Series();
        XYChart.Series series1 = new XYChart.Series();
        series.setName(name);

        for(Map.Entry<Integer,Double> entry:a.entrySet()){
            XYChart.Data data = new XYChart.Data(entry.getKey(),entry.getValue());
            data.setNode(new HoverNode(entry.getValue(),this.series.size()));
            series.getData().add(data);
            series1.getData().add(new XYChart.Data(""+entry.getKey(),entry.getValue()));
        }

        this.series.add(series);
        this.series1.add(series1);
        lineChart.getData().add(series);
        barChart.getData().add(series1);


    }
    public void switchGraph(String graphName){
        pane.getChildren().clear();
        switch(graphName) {
            case "LineGraph": pane.getChildren().add(lineChart);break;
            case "BarChart": pane.getChildren().add(barChart);break;
        }
    }

    /**
     *
     * @return
     */
    public StackPane getGraph(){
        return pane;
    }
    public void reset(){
        series.clear();
        series1.clear();
        series2.clear();
        lineChart.getData().clear();
        barChart.getData().clear();
    }
    class HoverNode extends StackPane{
        public HoverNode(double value, int size){
            BigDecimal bd = new BigDecimal(value);
            bd = bd.round(new MathContext(3));
            double rounded = bd.doubleValue();
            setPrefSize(15,15);
            final Label label = new Label(rounded+"");
            label.getStyleClass().addAll("default-color"+size, "chart-line-symbol", "chart-series-line");
            label.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
            label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    getChildren().setAll(label);
                    setCursor(Cursor.NONE);
                    toFront();
                }
            });
            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    getChildren().clear();
                    setCursor(Cursor.CROSSHAIR);
                }
            });
        }
    }
}
