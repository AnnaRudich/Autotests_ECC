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
  @DepreciationType INT = 1
AS
	SET NOCOUNT ON

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
         ([PseudoCategory], [InsuranceCompany], [ReductionRule])
  VALUES (@PseudoCategoryId, @CompanyID, @ReductionRuleId)

	SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_redrule]