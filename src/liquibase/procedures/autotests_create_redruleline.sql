--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_redruleline]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_create_redruleline]
GO

CREATE PROCEDURE [dbo].[autotests_create_redruleline]
  @LineDescription VARCHAR(60),
  @ReductionRuleDescription VARCHAR(60),
  @AgeFrom INT,
  @AgeTo INT,
  @PriceFrom DECIMAL(15,2),
  @PriceTo DECIMAL(15,2),
  @ClaimReduction FLOAT,
  @CashReduction FLOAT,
  @Sequence INT
AS
	SET NOCOUNT ON

  DECLARE @ReductionRuleId INT = (SELECT TOP (1000) [ReductionRuleID], [Description]
                                    FROM [dbo].[ReductionRule] where [Description] = @ReductionRuleDescription)
  INSERT INTO [dbo].[ReductionRuleLine]
         ([Description], [Sequence], [ClaimReduction], [CashReduction], [NewItemCode], [ReductionRule], [AgeFrom], [AgeTo], [priceRangeFrom], [priceRangeTo], [claimantRating])
  VALUES ( @LineDescription, @Sequence, @ClaimReduction, @CashReduction, 'O', @ReductionRuleId, @AgeFrom, @AgeTo, @PriceFrom, @PriceTo, 0)

	SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_redruleline]