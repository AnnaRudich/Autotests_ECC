package com.scalepoint.automation.utils.reports;

import com.aventstack.extentreports.ExtentTest;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Test {

    protected ExtentTest extentTest;
    protected List<Node> nodes = new LinkedList<>();

    protected Test(ExtentTest extentTest) {

        this.extentTest = extentTest;
    }

    public void createChart(){

        try {

            new Chart().createChart(extentTest);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    public String getName() {

        return extentTest.getModel().getName();
    }

    public ExtentTest getExtentTest() {

        return extentTest;
    }

    public Node getNode(String nodeName,  Date startDate){

        synchronized (this) {

            Node node = nodes.stream()
                    .filter(n -> n.getName().equals(nodeName))
                    .findFirst()
                    .orElseGet(() -> addNode(nodeName, startDate));

            return node;
        }
    }

    private Node addNode(String nodeName, Date startDate){

        Node node = new Node(extentTest.createNode(nodeName), startDate);
        nodes.add(node);

        return node;
    }
}
