--liquibase formatted sql
--changeset aru:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_fix_company_codes]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_fix_company_codes]
GO


CREATE PROCEDURE [dbo].[autotests_fix_company_codes]
@ICRFNBR int,
@NAME nvarchar(254),
AS
	SET NOCOUNT ON

	UPDATE [dbo].[INSCOMP] SET [CompanyCode] = UPPER(@NAME) , [UnifiedCompanyCode] = LOWER(@NAME) WHERE [ICRFNBR] = @ICRFNBR


	SET NOCOUNT OFF