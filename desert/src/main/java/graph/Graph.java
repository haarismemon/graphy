package main.java.graph;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import main.java.api.Indicator;
import main.java.api.Query;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import main.java.controller.SelectEvent;
import main.java.view.MainView;

/**
 * This class represents a Graph object that stores the types of graphs, and information about the query.
 */
public class Graph {

    private LineChart lineChart;
    private BarChart barChart;
    private PieChart pieChart;
    private StackPane pane;
    private Query query;
    private String title;
    private String graphType;
    private MainView mainView;

    /**
     * Event handler for the select graph action
     */
    private ObjectProperty<EventHandler<SelectEvent>> selectGraphAction = new SimpleObjectProperty<EventHandler<SelectEvent>>();

    /**
     * Creates the graph object that contains all three graphType of the same data.
     * @param graphName - Name of the type of graph
     */
    public Graph(MainView mainView, String graphName){
        this.mainView = mainView;
        lineChart = new LineChart(new NumberAxis(),new NumberAxis());
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);
        barChart = new BarChart(new CategoryAxis(),new NumberAxis());
        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
        pieChart = new PieChart();
        barChart.setLegendVisible(false);
        barChart.setAnimated(false);


        NumberAxis xAxis = (NumberAxis)lineChart.getXAxis();
        NumberAxis yAxis = (NumberAxis)lineChart.getYAxis();
        yAxis.setAutoRanging(true);
        yAxis.setForceZeroInRange(false);
        xAxis.setForceZeroInRange(false);
        xAxis.setLabel("Year");

        CategoryAxis cxAxis = (CategoryAxis)barChart.getXAxis();
        cxAxis.setLabel("Year");
        yAxis = (NumberAxis)barChart.getYAxis();
        yAxis.setAutoRanging(true);
        yAxis.setForceZeroInRange(false);


        setGraphName(graphName);
        pane = new StackPane();
        pane.getStylesheets().add("css/graph.css");
        pane.setOnMouseClicked((event) -> {
            selectGraphAction.get().handle(new SelectEvent(this));
            mainView.showInspectorPane();
        }); 
        switchGraph("LineGraph");

    }

    /**
     * Set title for the graph
     * @param name name of the graph
     */
    public void setGraphName(String name){
        this.title = name;
        lineChart.setTitle(name);
        barChart.setTitle(name);
        pieChart.setTitle(name);
    }

    /**
     * Sets the Y axis Label of the graph.
     * @param unit - The unit label for the graph
     */
    public void setYaxis(String unit){
        lineChart.getYAxis().setLabel(unit);
        barChart.getYAxis().setLabel(unit);
    }

    /**
     * Gets the title string of the graph
     * @return the title string of the graph
     */
    public String getTitle(){
        return title;
    }

    /**
     * Adds a data to the Graphs in form of series. Each series represent a query.
     * @param seriesName name of series for the query to show in legend
     * @param query - the query to be plotted in the graph
     */
    public void addSeries(String seriesName, Query query){
        this.query = query;

        changeColor(query.getColour());

        Map<Integer, Double> series = query.getData();
        XYChart.Series line = new XYChart.Series();
        XYChart.Series bar = new XYChart.Series();
        line.setName(seriesName);
        bar.setName(seriesName);

        Double last = 0d;
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for(Map.Entry<Integer,Double> entry:series.entrySet()){
            XYChart.Data data = new XYChart.Data(entry.getKey(),entry.getValue());
            data.setNode(new HoverNode(entry.getValue(),this.lineChart.getData().size()));
            line.getData().add(data);
            bar.getData().add(new XYChart.Data(""+entry.getKey(),entry.getValue()));
            PieChart.Data pieData = new PieChart.Data("" + entry.getKey(), entry.getValue());
            pieChartData.add(pieData);
            last = entry.getValue();
        }
        pieChart.setData(pieChartData);


        lineChart.getData().add(line);
        lineChart.getYAxis().setLabel(Indicator.getUnit(query.getIndicatorName()));
        barChart.getData().add(bar);
        barChart.getYAxis().setLabel(Indicator.getUnit(query.getIndicatorName()));
        pieChart.setLegendVisible(false);
    }

    /**
     * Change color of the graph visualisation
     * @param colorCode - the hex code of the color to change to
     */
    public void changeColor(String colorCode) {
        lineChart.setStyle("CHART_COLOR_1: " + colorCode +";");
        barChart.setStyle("CHART_COLOR_1: " + colorCode +";");
        pieChart.setStyle("CHART_COLOR_1: " + colorCode +";");
    }

    /**
     * Change the graph contained in pane to the sellected graph type
     * Select graph type using the parameter graph name
     * @param graphName Graph type name(one of three values : LineGraph   BarChart    PieChart)
     */
    public void switchGraph(String graphName){
        this.graphType = graphName;
        pane.getChildren().clear();
        switch(graphName) {
            case "LineGraph": pane.getChildren().add(lineChart);break;
            case "BarChart": pane.getChildren().add(barChart);break;
            case "PieChart": pane.getChildren().add(pieChart);break;
        }
    }

    /**
     * Gets the graph type
     * @return the graph type
     */
    public String getGraphType(){
        return graphType;
    }

    /**
     * returns a StackPane containing a one of three following graphs: LineGraph, BarGraph or PieChart
     * @return StackPane with graph
     */
    public StackPane getGraph(){
        return pane;
    }

    /**
     * clears data for this lineChart, barChart, and pieChart
     */
    public void reset(){
        lineChart.getData().clear();
        barChart.getData().clear();
        pieChart.getData().clear();
    }

    /**
     * Gets the query object that the graph represents
     * @return the query object stored in the graph
     */
    public Query getQuery(){
        return query;
    }

    /**
     * create handler to select a graph
     */
    public void selectGraphHandler(EventHandler<SelectEvent> handler) {
        selectGraphAction.set(handler);
    }

    /**
     * HoverNode Class is class for the lineChart.
     * Creates Hovering Node to show the value of data on the graph rounded to two significant figure
     */
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
