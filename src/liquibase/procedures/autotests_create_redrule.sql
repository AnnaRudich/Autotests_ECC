--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_redrule]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_create_redrule]
GO

CREATE PROCEDURE [dbo].[autotests_create_redrule]
  @RuleName VARCHAR(20),
  @CompanyName VARCHAR(32),
  @PseudoCategoryText VARCHAR(510),
  @PseudoCategoryGroupName VARCHAR(254),
  @LineDescription VARCHAR(60),
  @AgeFrom INT,
  @AgeTo INT,
  @PriceFrom DECIMAL(15,2),
  @PriceTo DECIMAL(15,2),
  @ClaimReduction FLOAT,
  @CashReduction FLOAT,
  @Sequence INT,
  @Policy VARCHAR(100),
  @DepreciationType INT = 1,
AS

	SET NOCOUNT ON

  IF EXISTS (SELECT * FROM [dbo].[ReductionRule] RR
    JOIN [dbo].[ReductionRuleLine] RRL ON RR.[ReductionRuleID] = RRL.[ReductionRule]
    WHERE RR.[Description] = @RuleName AND RRL.[Sequence] = @Sequence)
  RETURN

	DECLARE @CompanyID int
  IF @CompanyName IS NOT NULL
    SET @CompanyID = (select ICRFNBR from InsComp where CompanyCode = @CompanyName)

	DECLARE @PseudoCategoryId int = (SELECT pseudocategoryid FROM PsuedoCategory pc
    JOIN Text_Pseudocat tp on pc.PseudoCategoryText = tp.textid
    JOIN PSEUDOCAT_Group pg on pc.PseudoCatGroupId =  pg.pseudoCatGroupId
    JOIN Text_PSEUDOCAT_Group tpg on pg.groupTextId = tpg.textid
    WHERE name = @PseudoCategoryText and groupname = @PseudoCategoryGroupName)

	INSERT INTO [dbo].[ReductionRule]
         ([Description], [PublishState], [DepreciationTypeId], [MaxDepreciationPct], [LifeSpan], [YearsBeforeReduction])
  VALUES (@RuleName, 1, @DepreciationType, 100, 0, 0)
  DECLARE @ReductionRuleId INT = SCOPE_IDENTITY()

  INSERT INTO [dbo].[ReductionRuleConfiguration]
         ([PseudoCategory], [InsuranceCompany], [ReductionRule], [Policy])
  VALUES (@PseudoCategoryId, @CompanyID, @ReductionRuleId, @Policy)

  INSERT INTO [dbo].[ReductionRuleLine]
         ([Description], [Sequence], [ClaimReduction], [CashReduction], [NewItemCode], [ReductionRule], [AgeFrom], [AgeTo], [priceRangeFrom], [priceRangeTo], [claimantRating])
  VALUES ( @LineDescription, @Sequence, @ClaimReduction, @CashReduction, 'O', @ReductionRuleId, @AgeFrom, @AgeTo, @PriceFrom, @PriceTo, 0)

	SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_redrule]