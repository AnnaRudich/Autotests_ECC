package com.scalepoint.automation.utils.testng;

import com.scalepoint.automation.testGroups.TestGroups;
import org.testng.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HealthCheckListener implements ISuiteListener, IMethodInterceptor {

    private boolean healthCheckFailed = false;
    List<Suite> includedSuites = Arrays.asList(Suite.REGRESSION, Suite.QUNIT, Suite.SCALEPOINT_ID);

    @Override
    public void onStart(ISuite iSuite) {}

    @Override
    public void onFinish(ISuite iSuite) {

        if(Suite.findSuite(iSuite.getName()).equals(Suite.HEALTH_CHECK)){

            boolean test = isSuitePassed(iSuite);
            healthCheckFailed = !test;
        }
    }

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> list, ITestContext iTestContext) {

        ISuite suite = iTestContext.getSuite();

        if (includedSuites.contains(Suite.findSuite(suite.getName())) && healthCheckFailed) {

            return new LinkedList<>();
        }

        try {

            list = validateIncluded(list, iTestContext);
        } catch (IncorrectIncludeSyntax incorrectIncludeSyntax) {

            incorrectIncludeSyntax.printStackTrace();
            throw new RuntimeException(incorrectIncludeSyntax);
        }

        return list;
    }

    private List removeScalepointIdTestsFromRegressionSuite(List<IMethodInstance> list){

        return list.stream()
                .filter(iMethodInstance -> !Arrays.stream(iMethodInstance.getMethod().getGroups())
                        .anyMatch(group -> group.equals(TestGroups.SCALEPOINT_ID)) && !Suite.findSuite(iMethodInstance.getMethod().getXmlTest().getSuite().getName()).equals(Suite.REGRESSION))
                .collect(Collectors.toList());
    }

    private boolean isSuitePassed(ISuite iSuite){

        return iSuite
                .getResults()
                .values()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException())
                .getTestContext()
                .getFailedTests()
                .size() == 0;
    }

    private List validateIncluded(List<IMethodInstance> list, ITestContext iTestContext) throws IncorrectIncludeSyntax {

        String included = iTestContext.getSuite().getParameter("included");

        if (!included.equals("")) {

            IncludeGroups includeGroups = new IncludeGroups(included);
            if(includeGroups.matches()){

                return includeGroups.matcher(list);
            }
            IncludeMethods includeMethods = new IncludeMethods(included);
            if(includeMethods.matches()){

                return includeMethods.matcher(list);
            }

            throw new IncorrectIncludeSyntax(String.format("Following syntax is incorrect: %s",included));
        }

        return list;
    }

    abstract class Include {

        protected Matcher matcher;

        protected abstract Stream<IMethodInstance> filter(List<IMethodInstance> filteredList, String filter);
        protected boolean matches(){
            return matcher.matches();
        }
        protected List<IMethodInstance> matcher(List<IMethodInstance> list) {

            if (matches()) {

                List<String> includedList = Arrays.asList(matcher.group(2).split(";"));

                List<IMethodInstance> filteredList = new ArrayList<>();

                for (String f : includedList) {

                    filteredList.addAll(filter(list, f).collect(Collectors.toList()));
                }

                return filteredList;
            }

            return list;
        }
    }

    class IncludeGroups extends HealthCheckListener.Include {

        IncludeGroups(String included){

            matcher = Pattern.compile("(RetestGroups:)((\\w+;)+)").matcher(included);
        }

        protected Stream<IMethodInstance> filter(List<IMethodInstance> filteredList, String filter) {

            return filteredList
                    .stream()
                    .filter(method -> Arrays.stream(method.getMethod().getGroups()).anyMatch(g -> g.equals(filter)));
        }
    }

    class IncludeMethods extends HealthCheckListener.Include {

        IncludeMethods(String included){

            matcher = Pattern.compile("(RetestMethods:)((\\w+;)+)").matcher(included);
        }

        protected Stream<IMethodInstance> filter(List<IMethodInstance> filteredList, String filter){

            return filteredList
                    .stream()
                    .filter(m -> m.getMethod().getMethodName().equals(filter));
        }
    }

    public enum Suite{

        HEALTH_CHECK("HealthCheck"),
        REGRESSION("allTestsExceptRnV"),
        QUNIT("qunit"),
        SCALEPOINT_ID ("scalepointId"),
        ALL("All");

        private final String suiteName;

        Suite(String suiteName) {

            this.suiteName = suiteName;
        }

        public static Suite findSuite(String suiteName){

            return Arrays.stream(Suite.values())
                    .filter(performanceSuite ->  suiteName.contains(performanceSuite.suiteName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(suiteName));
        }
    }

}
