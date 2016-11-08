--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_redrule_tryg]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_create_redrule_tryg]
GO

CREATE PROCEDURE [dbo].[autotests_create_redrule_tryg]
AS

	SET NOCOUNT ON
	BEGIN TRANSACTION

  DECLARE @RuleName varchar(20) = 'Autotest-MusikRule'
  IF EXISTS (SELECT * FROM [dbo].[ReductionRule] WHERE [Description] = @RuleName)
    RETURN

	DECLARE @TrygCompanyID int = (select ICRFNBR from InsComp where CompanyCode = 'TRYGFORSIKRING')
	DECLARE @PseudoCategoryId int = 15 -- Musik, Film & Spil -> Øvrige

	INSERT INTO [dbo].[ReductionRule]
         ([Description], [PublishState], [DepreciationTypeId], [MaxDepreciationPct], [LifeSpan], [YearsBeforeReduction])
  VALUES (@RuleName, 1, 1, 100, 0, 0)
  DECLARE @ReductionRuleId INT = SCOPE_IDENTITY()

  INSERT INTO [dbo].[ReductionRuleConfiguration]
         ([PseudoCategory], [InsuranceCompany], [ReductionRule])
  VALUES (@PseudoCategoryId, @TrygCompanyID, @ReductionRuleId)

  INSERT INTO [dbo].[ReductionRuleLine]
         ([Description], [Sequence], [ClaimReduction], [CashReduction], [NewItemCode], [ReductionRule], [AgeFrom], [AgeTo], [priceRangeFrom], [priceRangeTo], [claimantRating])
  VALUES ('36-37', 1, 0.2, 0, 'O', @ReductionRuleId, 36, 37, 10, 20, 0)

	COMMIT TRANSACTION

	SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_redrule_tryg]