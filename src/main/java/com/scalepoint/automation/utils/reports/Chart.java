package com.scalepoint.automation.utils.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Test;
import lombok.AllArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Chart {

    private static final String FILE_EXTENSION = ".jpeg";
    private static final String SUB_CATEGORY_AXIS_LABEL = "LABEL";
    private static final String DOMAIN_AXIS_LABEL = "y";
    private static final String RANGE_AXIS_LABEL = "LOAD";
    private static final int WIDTH= 1000;
    private static final int HEIGHT= 600;
    private static final double CATEGORY_MARGIN = 0.05;
    private static final double ITEM_MARGIN = 0.0;

    private ExtentTest extentTest;
    private File file;
    private DefaultCategoryDataset dataset;
    private GroupedStackedBarRenderer renderer;
    private SubCategoryAxis domainAxis;
    private KeyToGroupMap map;
    private JFreeChart histogram;

    public Chart(ExtentTest extentTest){

        this.extentTest = extentTest;
        file = new File(extentTest.getModel().getName().concat(FILE_EXTENSION));
        dataset = new DefaultCategoryDataset();
        renderer = new GroupedStackedBarRenderer();
        domainAxis = new SubCategoryAxis(SUB_CATEGORY_AXIS_LABEL);
    }

    public void createChart() throws IOException {

        List<String> methods = extentTest.getModel().getChildren().stream()
                .map(node ->
                        node.getChildren().stream()
                                .map(method -> new ResultsPopulator(method, node.getName()).populateResults())
                                .collect(Collectors.toList()))
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        map = getKeyToGroupMap(methods);

        histogram = ChartFactory
                .createStackedBarChart(
                        extentTest.getModel().getName(),
                        DOMAIN_AXIS_LABEL, RANGE_AXIS_LABEL,
                        dataset,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        true);

        configureDomainAxis();
        configureRenderer();
        configureCategoryPlot();

        ChartUtilities.saveChartAsJPEG(file, histogram, WIDTH,HEIGHT);
        extentTest.addScreenCaptureFromPath(file.getPath());
    }

    private KeyToGroupMap getKeyToGroupMap(List<String> methods){

        KeyToGroupMap map = new KeyToGroupMap(methods.iterator().next());

        methods
                .forEach(methodName -> {

                    map.mapKeyToGroup(methodName.concat(Status.PASS.getName()), methodName);
                    map.mapKeyToGroup(methodName.concat(Status.FAIL.getName()), methodName);
                    map.mapKeyToGroup(methodName.concat(Status.SKIP.getName()), methodName);
                    domainAxis.addSubCategory(methodName);
                });

        return map;
    }

    private void configureDomainAxis(){

        domainAxis.setCategoryMargin(CATEGORY_MARGIN);
    }

    private void configureCategoryPlot(){

        CategoryPlot plot = (CategoryPlot) histogram.getPlot();
        plot.setDomainAxis(domainAxis);
        plot.setRenderer(renderer);
        plot.setFixedLegendItems(new LegendItemCollection());
    }

    private void configureRenderer(){

        renderer.setSeriesToGroupMap(map);
        renderer.setItemMargin(ITEM_MARGIN);
        setSeriesPaints();
    }

    private void setSeriesPaints(){

        dataset.getRowKeys().stream()
                .forEach(key -> {

                    setPaintByStatus(key, Status.PASS, Color.GREEN);
                    setPaintByStatus(key, Status.FAIL, Color.RED);
                    setPaintByStatus(key, Status.SKIP, Color.YELLOW);
                });
    }

    private void setPaintByStatus(Object key, Status status, Color color){

        if(((String) key).contains(status.toString())){

            int rowIndex = dataset.getRowIndex((Comparable) key);
            renderer.setSeriesPaint(rowIndex, color);
        }
    }

    @AllArgsConstructor
    private class ResultsPopulator{

        Test method;
        String nodeName;

        public String populateResults(){

            setValue(Status.FAIL);
            setValue(Status.PASS);
            setValue(Status.SKIP);

            return method.getName();
        }

        private void setValue(Status status){

            Long passed = method.getLogs().stream().filter(log ->

                    log.getStatus().equals(status))
                    .count();

            dataset.addValue(passed.doubleValue(), method.getName().concat(status.getName()), nodeName);
        }
    }
}
