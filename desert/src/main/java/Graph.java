import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Graph {
    String name;
    LineChart lineChart;
    ArrayList<XYChart.Series> series;


    public Graph(String graphName){
        name = graphName;
        series = new ArrayList<XYChart.Series>();
        NumberAxis xAxis = new NumberAxis();
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(true);
        xAxis.setTickUnit(1d);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setForceZeroInRange(false);
        yAxis.setAutoRanging(true);
        lineChart = new LineChart(xAxis,yAxis);

    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSeries(String name, TreeMap<Integer, Double> a){
        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        for(Map.Entry<Integer,Double> entry:a.entrySet()){
            series.getData().add(new XYChart.Data(entry.getKey(),entry.getValue()));
        }
        this.series.add(series);
        lineChart.getData().add(series);
    }
    public LineChart getGraph(){
        return lineChart;
    }
    public void reset(){
        series.clear();
        lineChart.getData().clear();
    }
}
