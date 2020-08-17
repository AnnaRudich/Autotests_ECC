package com.scalepoint.automation.utils.reports;

import com.aventstack.extentreports.ExtentTest;
import com.scalepoint.automation.utils.Configuration;
import org.testng.ITestContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class Test {

    private ExtentTest extentTest;
    private List<Node> nodes = new LinkedList<>();
    private ITestContext iTestContext = null;
    private Date startDate = null;
    private Date endDate;


    public Test(ExtentTest extentTest) {

        this.extentTest = extentTest;
    }

    public Test createChart(){

        try {

            new Chart(extentTest).createChart();
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        extentTest.info(String.format(
                "Start Time: %s; End Time: %s",
                startDate.toString(),
                endDate.toString()))
                .info(new PrometheusQuery()
                        .getLinkToGrafana(
                                startDate.toInstant().toEpochMilli(),
                                endDate.toInstant().toEpochMilli()))
                .info(new PrometheusQuery()
                        .getLinkToGraylog(
                                startDate,
                                endDate));
        getCPUUsage();
        getMemoryUsage();
        getThreads();
        getGCMetrics();
        getConnections();

        return this;
    }

    public String getName() {

        return extentTest.getModel().getName();
    }

    public ExtentTest getExtentTest() {

        return extentTest;
    }

    public Node setNode(String nodeName, ITestContext iTestContext) {

        if(startDate == null){

            startDate = iTestContext.getStartDate();
        }

        endDate = iTestContext.getEndDate();

        this.iTestContext = iTestContext;

        synchronized (this) {

            Node node = nodes.stream()
                    .filter(n -> n.getName().equals(nodeName))
                    .findFirst()
                    .orElseGet(() -> addNode(nodeName));

            return node.setTestResults();
        }
    }

    private Test getCPUUsage(){

        ArrayList responseSystemCPUUsage = getQuery(new PrometheusQuery().getEccQuery("system_cpu_usage", null));
        ArrayList responseProcessCPUUsage = getQuery(new PrometheusQuery().getEccQuery("process_cpu_usage", null));

        List valuesSystemCPUUsageValues = getValues(responseSystemCPUUsage);
        List valuesProcessCPUUsage = getValues(responseProcessCPUUsage);

        try {

            extentTest
                    .info(String.format(
                            "System CPU usage: %s; Process CPU usage: %s",
                            String.valueOf(
                                    max(valuesSystemCPUUsageValues)
                                            .multiply(BigDecimal.valueOf(100))
                                            .setScale(2, RoundingMode.HALF_UP))
                                    .concat("%"),
                            String.valueOf(
                                    max(valuesProcessCPUUsage)
                                            .multiply(BigDecimal.valueOf(100))
                                            .setScale(2, RoundingMode.HALF_UP))
                                    .concat("%")));
        } catch (Throwable throwable) {

            throw new RuntimeException(throwable);
        }

        return this;
    }

    public Test getMemoryUsage(){

        Map<String, String> heap = new HashMap<>();
        heap.put("area", "heap");
        Map<String, String> nonheap = new HashMap<>();
        heap.put("area", "nonheap");

        ArrayList responseJVMMemeoryUsedBytesHeap = getQuery(new PrometheusQuery().getSumEccQuery("jvm_memory_used_bytes", heap));
        ArrayList responseJVMMemoryMaxBytesHeap = getQuery(new PrometheusQuery().getSumEccQuery("jvm_memory_max_bytes", heap));
        ArrayList responseJVMMemeoryUsedBytesNonHeap = getQuery(new PrometheusQuery().getSumEccQuery("jvm_memory_used_bytes", nonheap));
        ArrayList responseJVMMemoryMaxBytesNonHeap = getQuery(new PrometheusQuery().getSumEccQuery("jvm_memory_max_bytes", nonheap));

        List valuesJVMMemoryUsedBytesHeap = getValues(responseJVMMemeoryUsedBytesHeap);
        List valuesJVMMemoryMaxBytesHeap = getValues(responseJVMMemoryMaxBytesHeap);
        List valuesJVMMemoryUsedBytesNonHeap = getValues(responseJVMMemeoryUsedBytesNonHeap);
        List valuesJVMMemoryMaxBytesNonHeap = getValues(responseJVMMemoryMaxBytesNonHeap);

        List usedMemoryHeap = getUsedMemoryRatio(valuesJVMMemoryUsedBytesHeap, valuesJVMMemoryMaxBytesHeap);
        List usedMemoryNonHeap = getUsedMemoryRatio(valuesJVMMemoryUsedBytesNonHeap, valuesJVMMemoryMaxBytesNonHeap);

        try {

            extentTest
                    .info(String.format("Heap memory usage: %s; Non-heap memory usage: %s",
                            String.valueOf(
                                    max(usedMemoryHeap)
                                            .multiply(BigDecimal.valueOf(100))
                                            .setScale(2, RoundingMode.HALF_UP))
                                    .concat("%"),
                            String.valueOf(
                                    max(usedMemoryNonHeap)
                                            .multiply(BigDecimal.valueOf(100))
                                            .setScale(2, RoundingMode.HALF_UP))
                                    .concat("%")
                    ));
        } catch (Throwable throwable) {

            throw new RuntimeException(throwable);
        }

        return this;
    }

    private Test getGCMetrics(){


        ArrayList responseJvmGcPauseSecondsCount = getQuery(new PrometheusQuery().getEccQuery("jvm_gc_pause_seconds_count", null));
        ArrayList responseJvmGcPauseSecondsSum = getQuery(new PrometheusQuery().getEccQuery("jvm_gc_pause_seconds_sum", null));

        List valuesJvmGcPauseSecondsCount = getValues(responseJvmGcPauseSecondsCount);
        List valuesJvmGcPauseSecondsSum = getValues(responseJvmGcPauseSecondsSum);

        try {

            extentTest
                    .info(
                            String.format(
                                    "GC pause seconds count: %s; GC pause seconds sum: %s",
                                    String.valueOf(
                                            max(valuesJvmGcPauseSecondsCount)),
                                    String.valueOf(
                                            max(valuesJvmGcPauseSecondsSum))));
        } catch (Throwable throwable) {

            throw new RuntimeException(throwable);
        }

        return this;
    }

    private Test getThreads(){

        ArrayList responseDaemonThreads = getQuery(new PrometheusQuery().getEccQuery("jvm_threads_daemon_threads", null));
        ArrayList responseLiveThreads = getQuery(new PrometheusQuery().getEccQuery("jvm_threads_live_threads", null));
        ArrayList responsePeakThreads = getQuery(new PrometheusQuery().getEccQuery("jvm_threads_peak_threads", null));

        List valuesDaemonThreads = getValues(responseDaemonThreads);
        List valuesLiveThreads = getValues(responseLiveThreads);
        List valuesPeakThreads = getValues(responsePeakThreads);


        try {

            extentTest
                    .info(String.format(
                            "Daemon Threads: %s; Live Threads: %s; Peak Threads: %s",
                            String.valueOf(max(valuesDaemonThreads)),
                            String.valueOf(max(valuesLiveThreads)),
                            String.valueOf(max(valuesPeakThreads))));
        } catch (Throwable throwable) {

            throw new RuntimeException(throwable);
        }

        return this;
    }

    private Test getConnections(){

        Map<String, String> pool = new HashMap<>();
        pool.put("pool", "ECC");

        ArrayList responseHikaricpConnections = getQuery(new PrometheusQuery().getEccQuery("hikaricp_connections", pool));
        ArrayList responseHikaricpConnectionsActive = getQuery(new PrometheusQuery().getEccQuery("hikaricp_connections_active", pool));
        ArrayList responseHikaricpConnectionsIdle = getQuery(new PrometheusQuery().getEccQuery("hikaricp_connections_idle", pool));
        ArrayList responseHikaricpConnectionsPending = getQuery(new PrometheusQuery().getEccQuery("hikaricp_connections_pending", pool));
        ArrayList responseHikaricpConnectionsTimeoutTotal = getQuery(new PrometheusQuery().getEccQuery("hikaricp_connections_timeout_total", pool));

        List valuesHikaricpConnections = getValues(responseHikaricpConnections);
        List valuesHikaricpConnectionsActive = getValues(responseHikaricpConnectionsActive);
        List valuesHikaricpConnectionsIdle = getValues(responseHikaricpConnectionsIdle);
        List valuesHikaricpConnectionsPending = getValues(responseHikaricpConnectionsPending);
        List valuesHikaricpConnectionsTimeoutTotal = getValues(responseHikaricpConnectionsTimeoutTotal);

        try {

            extentTest
                    .info(String.format(
                            "Connections: %s, Active connections: %s; Connections Idle: %s; Connections pending: %s, Connections Timeout total: %s",
                            String.valueOf(max(valuesHikaricpConnections)),
                            String.valueOf(max(valuesHikaricpConnectionsActive)),
                            String.valueOf(max(valuesHikaricpConnectionsIdle)),
                            String.valueOf(max(valuesHikaricpConnectionsPending)),
                            String.valueOf(max(valuesHikaricpConnectionsTimeoutTotal))));
        } catch (Throwable throwable) {

            throw new RuntimeException(throwable);
        }

        return this;
    }

    private ArrayList getQuery(String query){

        return given().baseUri("https://ecc-monitoring.spcph.local/prometheus/api/v1/query_range")
                .log().all()
                .queryParam("query", query)
                .queryParam("start", startDate.toInstant().getEpochSecond())
                .queryParam("end", endDate.toInstant().getEpochSecond())
                .queryParam("step", "1")
                .queryParam("_", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond())
                .get()
                .then()
                .log().all()
                .extract().response()
                .getBody()
                .jsonPath()
                .get("data.result.values");
    }

    private List getValues(ArrayList valuesArrayList){

        List list;

        try {
            list = (List) valuesArrayList.stream()
                    .map(values -> {
                                try {

                                    return ((ArrayList) values).stream()
                                            .map(value -> {
                                                try {
                                                    return ((ArrayList) value).stream()
                                                            .filter(parameter -> parameter.getClass().equals(String.class))
                                                            .findFirst()
                                                            .orElseThrow(NoSuchElementException::new);
                                                } catch (Throwable throwable) {

                                                    throw new RuntimeException(throwable);
                                                }
                                            })
                                            .collect(Collectors.toList());
                                } catch (Throwable throwable) {

                                    throw new RuntimeException(throwable);
                                }
                            }
                    )
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
        } catch (Throwable throwable) {

            throw new RuntimeException(throwable);
        }

        return list;
    }

    public List getUsedMemoryRatio(List usedMemory, List maxMemory){

        Iterator memoryMaxBytesIterator = maxMemory.iterator();

        return (List) usedMemory
                .stream()
                .map(value ->
                        new BigDecimal(String.valueOf(value))
                                .divide(new BigDecimal(String.valueOf(memoryMaxBytesIterator.next())), 4,  RoundingMode.HALF_UP))
                .collect(Collectors.toList());
    }

    public BigDecimal max(List valuesList) throws Throwable {

        return (BigDecimal) valuesList.stream()
                .map(value -> new BigDecimal(String.valueOf(value)))
                .max(Comparator.naturalOrder())
                .orElseThrow(NoSuchElementException::new);
    }

    private Node addNode(String nodeName){

        Node node = new Node(extentTest.createNode(nodeName), iTestContext);
        nodes.add(node);

        return node;
    }

    class PrometheusQuery{

        private static final String BASE = "%s{instance=\"ecc-%s.spcph.local:81\", locale=~\"%s\", service=\"%s\"%s}";
        String instance;
        String locale;
        String service;

        public PrometheusQuery(){

            Matcher matcher = Pattern.compile("(qa\\d+)").matcher(Configuration.getServerUrl());
            matcher.find();

            instance = matcher.group();
            locale = Configuration.getLocale().getValue().toUpperCase();
            service = "ECC";
        }

        public String getEccQuery(String metric, Map<String, String> labels){

            return String.format(
                    BASE,
                    metric,
                    instance,
                    locale,
                    service,
                    labels == null ? "" : setLabels(labels));
        }

        public String getSumEccQuery(String metric, Map<String, String> labels){

            return String.format("sum(%s)", getEccQuery(metric, labels));
        }

        public String setLabels(Map<String, String> labels){

            return labels
                    .entrySet().stream()
                    .map(entry ->
                            String.format(
                                    ", %s=\"%s\"",
                                    entry.getKey(),
                                    entry.getValue()))
                    .collect(Collectors.joining());
        }

        public String getLinkToGrafana(long from, long to){

            String href = String.format("https://ecc-monitoring.spcph.local/grafana/d/uhPdYL_Zk/ecc-spring-boot-statistics" +
                    "?orgId=1" +
                    "&from=%s" +
                    "&to=%s" +
                    "&var-application=%s" +
                    "&var-instance=ecc-%s.spcph.local:81" +
                    "&var-locale=%s" +
                    "&var-hikaricp=%s" +
                    "&var-memory_pool_heap=ALL" +
                    "&var-memory_poll_nonheap=ALL", from, to, service, instance, locale, service);

            return String.format("<a href=%s>Grafana</a>", href);
        }

        public String getLinkToGraylog(Date from, Date to){

            String source = String.format("source:ecc-%s AND fields_logLevel:ERROR", instance);
            String rangeType = "absolute";

            String href = String.format(
                    "?q=%s" +
                    "&rangetype=%s" +
                    "&from=%s" +
                    "&to=%s",
                    URLEncoder.encode(source),
                    URLEncoder.encode(rangeType),
                    URLEncoder.encode(DateTimeFormatter.ISO_INSTANT.format(from.toInstant())),
                    URLEncoder.encode(DateTimeFormatter.ISO_INSTANT.format(to.toInstant())));

                return String.format("<a href=https://dev-graylog.spcph.local/search%s>Graylog</a>", href);
        }
    }
}
