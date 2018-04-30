package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Claim;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

public class ClaimSearchPage extends Page {

  @Override
  protected Page ensureWeAreOnPage() {
    return null;
  }

  @Override
  protected String getRelativeUrl() {
    return null;
  }

  @FindBy(id = "namefield")
  private WebElement namefieldInput;

  @FindBy(id = "customernumberfield")
  private WebElement customernumberfieldInput;

  @FindBy(id = "claimnofield")
  private WebElement claimnofieldInput;

  @FindBy(id = "phonefield")
  private WebElement phonefieldInput;

  @FindBy(id = "settlementdatefield")
  private WebElement settlementdatefieldInput;

  @FindBy(id = "claimhandler")
  private WebElement claimhandlerInput;

  @FindBy(id = "addressfield")
  private WebElement addressfieldInput;

  @FindBy(id = "company")
  private Select companySelect;

  @FindBy(id = "claimstate")
  private Select claimstateSelect;

  @FindBy(id = "postalfield")
  private WebElement postalfieldInput;

  @FindBy(id = "soeg")
  private WebElement soegButton;


  public List<ClaimRow> getClaimRows(){
    return $(".claim-search-table").findAll("tr")
            .stream().skip(1).map(ClaimRow::new).collect(Collectors.toList());
  }

  public ClaimSearchPage fillClaimNumber(String claimNumber) {
    claimnofieldInput.clear();
    claimnofieldInput.sendKeys(claimNumber);
    return this;
  }

  public ClaimSearchPage search() {
    soegButton.click();
    Wait.waitForAjaxCompleted();
    return this;
  }

  public static class ClaimRow {

    private String name;
    private String claimNumber;
    private String claimHandler;
    private String company;
    private String claimState;
    private String settlementDate;
    private String activeDate;

    ClaimRow(WebElement element) {
      this.name = element.findElement(By.xpath("./td[1]//label")).getText();
      this.claimNumber = element.findElement(By.xpath("./td[2]")).getText();
      this.claimHandler = element.findElement(By.xpath("./td[3]")).getText();
      this.company = element.findElement(By.xpath("./td[4]")).getText();
      this.claimState = element.findElement(By.xpath("./td[5]")).getText();
      this.settlementDate = element.findElement(By.xpath("./td[6]")).getText();
      this.activeDate = element.findElement(By.xpath("./td[7]")).getText();
    }

    public String getName() {
      return name;
    }

    public String getClaimNumber() {
      return claimNumber;
    }

    public String getClaimHandler() {
      return claimHandler;
    }

    public String getCompany() {
      return company;
    }

    public String getClaimState() {
      return claimState;
    }

    public String getSettlementDate() {
      return settlementDate;
    }

    public String getActiveDate() {
      return activeDate;
    }
  }

  public ClaimSearchPage doAssert(Claim claim, Consumer<ClaimSearchPage.Asserts> assertsFunc) {
    assertsFunc.accept(new ClaimSearchPage.Asserts(claim));
    return ClaimSearchPage.this;
  }

  public ClaimSearchPage doAssert(Consumer<ClaimSearchPage.Asserts> assertsFunc) {
    return doAssert(null, assertsFunc);
  }

  public class Asserts {

    private Claim claim;
    private List<ClaimRow> claimRows;

    public Asserts(Claim claim){
      this.claim = claim;
      this.claimRows = getClaimRows();
    }

    public Asserts isOnlyOnList(){
      assertThat(claimRows.stream().allMatch(claimRow -> claimRow.getClaimNumber().equals(this.claim.getClaimNumber()))).isTrue();
      return this;
    }

    public Asserts isClaimState(ClaimState state){
      assertThat(claimRows.stream().filter(claimRow -> claimRow.getClaimNumber().equals(this.claim.getClaimNumber())).findFirst().get().getClaimState())
              .isEqualToIgnoringCase(state.getStateFullText());
      return this;
    }

    public Asserts isClaimCompany(String companyName){
      assertThat(claimRows.stream().filter(claimRow -> claimRow.getClaimNumber().equals(this.claim.getClaimNumber())).findFirst().get().getCompany())
              .isEqualToIgnoringCase(companyName);
      return this;
    }
  }

  public enum ClaimState {

    IN_USE('W'),
    DELETED('D'),
    OPEN('P'),
    COMPLETED('S'),
    LOCKED_BY_ORDER('L'),
    CANCELED('X'),
    CLOSED_EXTERNALLY('E');

    private Character state;

    ClaimState(Character state){
      this.state = state;
    }

    public String getStateFullText(){
      return TestData.getClaimStates().get(this.state);
    }
  }

}
