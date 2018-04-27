package com.scalepoint.automation.pageobjects.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;

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
            .stream().map(ClaimRow::new).collect(Collectors.toList());
  }

  private static class ClaimRow {

    private String name;
    private String claimNumber;
    private String claimHandler;
    private String company;
    private String settlementDate;
    private String activeDate;

    public ClaimRow(WebElement element) {
      this.name = element.findElement(By.xpath("./td[1]//label")).getText();
      this.claimNumber = element.findElement(By.xpath("./td[2]")).getText();
      this.claimHandler = element.findElement(By.xpath("./td[3]")).getText();
      this.company = element.findElement(By.xpath("./td[4]")).getText();
      this.settlementDate = element.findElement(By.xpath("./td[5]")).getText();
      this.activeDate = element.findElement(By.xpath("./td[6]")).getText();
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

    public String getSettlementDate() {
      return settlementDate;
    }

    public String getActiveDate() {
      return activeDate;
    }
  }

}
