# automated test

Project for automating tests mostly
covers UI flows of ECC with some parts of Self Service.

## Setup and Configuration

Clone repo from git:
    `` ssh://git@bitbucket.spcph.local:7999/ecc/automatedtest.git ``

JDK 1.8 required<br>
https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

Maven 3.x required<br>
https://maven.apache.org/download.cgi

### DB config

Some tests are using windows authentication and integrated security to
connect to database. <br>
You can find jdbc urls in ```scr/main/resources-filtered/*.properties```

Pattern is ```application-<env_name>.properties``` -> env_name is taken from maven profiles
properties ```<activatedProperties>``` tag

pom.xml:

```xml
...
    <profiles>
        <profile>
            <id>localhost</id>
            <properties>
                <activatedProperties>localhost</activatedProperties>
            </properties>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
        </profile>
        ...
        <profile>
            <id>QA01</id>
            <properties>
                <activatedProperties>qa01</activatedProperties>
            </properties>
        </profile>
        <profile>
            <id>QA02</id>
            <properties>
                <activatedProperties>qa02</activatedProperties>
            </properties>
        </profile>
...
```


application.properties files is common for all and if same properties will be in
named application property file then it will be overridden.

If you don't want to use integrated security look in code for dataSource Beans
and add to it username and password

ecc -> ```@Bean(name = "eccDb")```

events-api -> ```@Bean(name = "eventApiDb")```


.BeansConfiguration.java
```java
    @Bean(name = "eccDb")
    @Primary
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
```

Before running tests tests data have to be injected to db.
There is liquibase maven plugin that runs all SQL scripts located in:
`src/liquibase`

just type from command line:
`mvn liquibase:update -Plocalhost`

or use Idea MavenProjects -> autotests -> plugins -> liquibase -> liquibase:update
using Idea MavenProjects do not forget to select correct profile.

Scripts are creating future-users, future-companies, vouchers, categories, reduction rules and more,
to check if data was added login using `autotest-sp /// 12341234` account or any other `autotest-future50 /// 12341234`

## Running tests

To run full regression suite:
`mvn clean test -PQA14 -Dmaven.test.skip=false -DsuiteXmlFile=suites/allTestsExceptRV -Dlocale=dk -U`

If you want to run single tests just find it in:
`src/main/java/com/scalepoint/automation/tests`
and press green arrow on method with @Test annotation

other tests suites that can be run are located in `suites/` folder
`smokeECC_testng.xml` is used for ecc smoke tests

in application*.properties files there is property called
`driver.type` it is used to tell on which browser you want to run tests.

all possible driver types for now:

.DriverType
```
    IE("ie.local"),
    IE_REMOTE("ie.remote"),
    FF_REMOTE("ff.remote"),
    CHROME("chrome.local"),
    CHROME_REMOTE("chrome.remote"),
    CHROME_ZALENIUM_LOCAL("chrome.zalenium.local"),
    CHROME_ZALENIUM_REMOTE("chrome.zalenium.remote"),
    FF("ff.local"),
    EDGE("edge.local");
```

also for single tests or test class you can specify driver type by annotation,
`@RunOn(DriverType)`

.runOn
```java
  @RunOn(DriverType.CHROME_REMOTE)
  @Test(dataProvider = "testDataProvider", description = "Search for open claim")
  public void searchClaim_open(User user, Claim claim) {
    ...
  }
```

### Local drivers

Local driver are for running tests locally, so it will try to open a browser on machine where
maven command or IntelliJ configurations is running.
<br>
For that purpose we are using https://github.com/bonigarcia/webdrivermanager
It downloads webdriver binaries automatically and setting up system properties for them.
<br>

### Remote drivers

Remote driver are for running tests on selenium grid hub.
Selenium hub is located on dev-ecc-tool's server and it is running inside docker container
<br>
Url to selenium grid console -> http://dev-ecc-tool02.spcph.local:4444/grid/console
<br>
<br>
Nodes:

    - 10.99.14.49 (ecc-aqa01.spcph.local)
    
    - 10.99.14.50 (ecc-aqa02.spcph.local)
    
    - 10.99.14.51 (ecc-aqa03.spcph.local)

have config for running tests on chrome and IE.
<br>
Login there using `ecc_auto /// 1q2w3e4r5tQAWSEDRFTG`

    C:\Webdriver -> there is NodeConfig.json and NodeStart.bat file

    C:\Drivers -> WebDriver binaries for chrome and IE

Hub remote url is set in application.properties file:
`hub.remote=http://dev-ecc-tool02.spcph.local:4444/wd/hub`

### Zalenium

Zalenium are for running tests using zalenium containers.
<br>
https://github.com/zalando/zalenium
<br>
Zalenium supports maneging selenium grid hub and nodes in docker, it creates
nodes dynamically, allows to record failed tests, connect to docker nodes using vnc,
store tests and tests results in zalenium dashboard.
<br>
Connection urls to zalenium are stored in application.properties files
`hub.remote.zalenium=http://dev-ecc-tool01:4444/wd/hub
 hub.local.zalenium=http://localhost:4444/wd/hub`
<br>
for now zalenium is disabled because it's not supports IE and need to be running in docker cluster
like kubernetes.

### Others drivers

FF and EDGE are added but not tests.

## Contributing

1. Create branch
2. Push changes
3. Open Pull Request
<br>

Tests should be located in:
`src/main/java/com/scalepoint/automation/tests`
<br>
Page Object should be in:
`src/main/java/com/scalepoint/automation/pageObjects`
<br>
Tests using only http api(no selenium) are located in:
`src/main/java/com/scalepoint/automation/tests/api`
<br>
Http api call are located in:
`src/main/java/com/scalepoint/automation/services/restService`
<br>
There are also some external api's like FtTemplates, SolrApi and more:
`src/main/java/com/scalepoint/automation/services/externalapi`
<br>
All tests are using data provider not for DDT but for providing test objects
```java
    @Test(dataProvider = "testDataProvider")
    public void charlie544_cancelSavedClaim(User user, Claim claim){
      ...
    }
```
In example you see User and Claim objects which fields will be populated by data provider
All available objects and it's file mappings are located in TestData.class
```java
public enum Data {
        EXISTING_SUPPLIER("ExistingSuppliers.xml", ExistingSuppliers.class),
        SUPPLIER("Supplier.xml", Supplier.class),
        SHOP("Shop.xml", Shop.class),
        VOUCHER("Voucher.xml", Voucher.class),
        SYSTEMCREDENTIALS("ExistingUsers.xml", ExistingUsers.class),
        NEWSYSTEMUSER("SystemUser.xml", SystemUser.class),
        CATEGORIES("Category.xml", Category.class),
        INSURANCECOMPANY("InsuranceCompany.xml", InsuranceCompany.class),
        REDUCTIONRULE("ReductionRule.xml", ReductionRule.class),
        ROLES("Roles.xml", Roles.class),
        CLAIM("Claim.xml", Claim.class),
        CLAIMITEM("ClaimItem.xml", ClaimItem.class),
        TEXTSEARCH("TextSearch.xml", TextSearch.class),
        MAILS("Mail.xml", Mail.class),
        ERRORS("Errors.xml", Errors.class),
        NOTIFICATIONS("Notifications.xml", Notifications.class),
        GENERICITEM("GenericItem.xml", GenericItem.class),
        PASSWORDVERIFICATION("PasswordsVerification.xml", PasswordsVerification.class),
        DEPRECIATIONTYPE("DepreciationType.xml", DepreciationType.class),
        RRLINEFIELDS("RRLinesFields.xml", RRLinesFields.class),
        SERVAGREEMENT("ServiceAgreement.xml", ServiceAgreement.class),
        ORDERDETAILS("OrderDetails.xml", OrderDetails.class),
        PRICERULE("PriceRule.xml", PriceRule.class),
        CLGROUP("ClaimLineGroup.xml", ClaimLineGroup.class),
        PAYMENTS("Payments.xml", Payments.class),
        ATTFILES("AttachmentFiles.xml", AttachmentFiles.class),
        DISCRETIONARYREASON("DiscretionaryReason.xml", DiscretionaryReason.class),
        CWA_CLAIM("Claim/ClaimRequest.json", ClaimRequest.class),
        ASSIGNMENT("Assignment.xml", Assignment.class),
        CLAIM_ITEM("Claim/ClaimItem.xml", InsertSettlementItem.class),
        ECC_INTEGRATION("Claim/EccIntegration.xml", EccIntegration.class),
        CLAIM_STATE("ClaimState.json", ClaimStates.class),
        ACQUIRED("Acquired.xml", Acquired.class);
}
```

## Teamcity Jobs

There are 3 Teamcity Jobs for autotests.

1. http://teamcity.spcph.local/viewType.html?buildTypeId=Ecc_EccAutoTests
<br> It's for running full regression or some manual builds.
<br> There is a trigger on this job that deploys latest 3.2 ECC, latest 2.1 SS to qa14
and run full regression suite on that.

2. http://teamcity.spcph.local/viewType.html?buildTypeId=Ecc_EccAutoTestsSmoke
<br> This Job is triggered after ECC build job run and deploys artifact from that build
to qa13 and run smokeECC test suite on that.
<br> Build chains: http://teamcity.spcph.local/viewType.html?buildTypeId=Ecc_EccAutoTestsSmoke&tab=buildTypeChains&branch_Ecc=__all_branches__

3. http://teamcity.spcph.local/viewType.html?buildTypeId=Ecc_AutotestsPrepareEnvironment
<br> This Job is for preparing environment for tests and not only.
<br> It allows you to restore DB's, run autotests liquibase scripts, restore solr indexes.
<br> Those scripts are used in both autotests jobs 'all' and 'smoke'.

Test run jobs are using small PowerShell script for checking if ecc is up.
This is added to TeamCity build step.
<br>

Check URL script:

.checkUrls.ps
```
function Get-UrlStatusCode([string] $Url){
    try
    {
        (Invoke-WebRequest -Uri $Url -TimeoutSec 10 -UseBasicParsing -DisableKeepAlive).StatusCode
    }
    catch [Net.WebException]
    {
        [int]$_.Exception.Response.StatusCode
    }
    catch [System.Exception]
    {
        echo $_.Exception.Message
    }
}

function checkUrl([string] $url, [int] $expectedStatusCode){
$sw = [diagnostics.stopwatch]::StartNew()

        [int]$statusCode
        while ($sw.elapsed -lt $timeout){
            write-host ("Checking url: " + $url)
            $statusCode = Get-UrlStatusCode $url
            if ($statusCode -eq $expectedStatusCode){
                write-host "OK"
                return
                }
            else {
                start-sleep -seconds 10
                write-host ("Url: " + $url +" returned " + $statusCode + " but should be " + $expectedStatusCode)
            }
        }
        if (!($statusCode -eq $expectedStatusCode)){
            write-host ("Timeout waiting for url: " + $url)
            exit 1;
        }
}


$timeout = new-timespan -Minutes 20

checkUrl 'http://ecc-%SERVER_NAME%.spcph.local:81/eccAdmin/dk/login.action' 200

if(%DB_RESTORE_ECC_DATABASE%){
    Invoke-Expression "./Reindexation.ps1 -eccHost 'http://ecc-%SERVER_NAME%.spcph.local:81' -versions %LOCALES_DEPLOY%"
}
```

