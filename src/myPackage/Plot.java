package myPackage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import java.awt.*;

public class Plot extends ApplicationFrame {
    public Plot(String applicationTitle, String chartTitle, double d1, double d2, double d3) {
        super(applicationTitle);
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle,
                "Number of Training Samples",
                "Average Accuracy",
                createDataset(d1, d2, d3),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(xylineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        final XYPlot plot = xylineChart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
        plot.setRenderer(renderer);

        NumberAxis domain = (NumberAxis) plot.getRangeAxis();
        domain.setRange( Math.max( 0.0, Math.min(d1, Math.min(d2, d3)) - 0.05),
                Math.min( 1.0, Math.max(d1, Math.max(d2, d3)) + 0.05));
        domain.setTickUnit(new NumberTickUnit(domain.getRange().getLength() / 6));
        domain.setVerticalTickLabels(true);

        setContentPane(chartPanel);
    }

    private XYDataset createDataset(double d1, double d2, double d3) {
        final XYSeries learningCurve = new XYSeries("learning curve");
        learningCurve.add(25, d1);
        learningCurve.add(50, d2);
        learningCurve.add(100, d3);

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(learningCurve);

        return dataset;
    }

}
