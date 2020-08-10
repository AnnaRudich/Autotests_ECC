package com.scalepoint.automation.utils.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
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

    public void createChart(ExtentTest extentTest) throws IOException {

        File file = new File(extentTest.getModel().getName().concat(".jpeg"));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        SubCategoryAxis domainAxis = new SubCategoryAxis("method");
        domainAxis.setCategoryMargin(0.05);


        List<String> methods = extentTest.getModel().getChildren().stream()
                .map(node ->
                        node.getChildren().stream()
                                .map(method ->
                                {
                                    Long passed = method.getLogs().stream().filter(log ->

                                            log.getStatus().equals(Status.PASS))
                                            .count();

                                    Long failed = method.getLogs().stream().filter(log ->

                                            log.getStatus().equals(Status.FAIL))
                                            .count();

                                    Long skipped = method.getLogs().stream().filter(log ->

                                            log.getStatus().equals(Status.SKIP))
                                            .count();

                                    dataset.addValue(passed.doubleValue(), method.getName().concat(Status.PASS.getName()), node.getName());
                                    dataset.addValue(failed.doubleValue(),  method.getName().concat(Status.FAIL.getName()), node.getName());
                                    dataset.addValue(skipped.doubleValue(),  method.getName().concat(Status.SKIP.getName()), node.getName());

                                    return method.getName();
                                })
                                .collect(Collectors.toList()))
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        KeyToGroupMap map = new KeyToGroupMap(methods.iterator().next());

        methods
                .forEach(methodName -> {

                    map.mapKeyToGroup(methodName.concat(Status.PASS.getName()), methodName);
                    map.mapKeyToGroup(methodName.concat(Status.FAIL.getName()), methodName);
                    map.mapKeyToGroup(methodName.concat(Status.SKIP.getName()), methodName);
                    domainAxis.addSubCategory(methodName);
                });

        renderer.setSeriesToGroupMap(map);
        renderer.setItemMargin(0.0);

        System.out.println("Row keys: " + dataset.getRowKeys());

        dataset.getRowKeys().stream()
                .forEach(key -> {

                    if(((String) key).contains(Status.PASS.toString())){

                        int rowIndex = dataset.getRowIndex((Comparable) key);
                        renderer.setSeriesPaint(rowIndex, Color.GREEN);
                    }
                    if(((String) key).contains(Status.FAIL.toString())){

                        int rowIndex = dataset.getRowIndex((Comparable) key);
                        renderer.setSeriesPaint(rowIndex, Color.RED);
                    }
                    if(((String) key).contains(Status.SKIP.toString())){

                        int rowIndex = dataset.getRowIndex((Comparable) key);
                        renderer.setSeriesPaint(rowIndex, Color.YELLOW);
                    }

                });

        JFreeChart hisogram = ChartFactory.createStackedBarChart("Norma", "y", "x", dataset, PlotOrientation.VERTICAL, true,true,true);

        CategoryPlot plot = (CategoryPlot) hisogram.getPlot();
        plot.setDomainAxis(domainAxis);
        //plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
        plot.setRenderer(renderer);
        plot.setFixedLegendItems(new LegendItemCollection());


        System.out.println("Groups: " + map.getGroups());


        ChartUtilities.saveChartAsJPEG(file, hisogram, 1000,600);
        extentTest.addScreenCaptureFromPath(file.getPath());
    }
}
