--liquibase formatted sql
--changeset aru:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_enable_pseudoCategoryRepairMap]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_enable_pseudoCategoryRepairMap]
GO


CREATE PROCEDURE [dbo].[autotests_enable_pseudoCategoryRepairMap]
@InsuranceCompanyName NVARCHAR(32)

AS
	SET NOCOUNT ON

	declare @FunctionTemplateId int = (select FTRFNBR from FUNCTEMPLATE where FTTMPLNAME = @InsuranceCompanyName)
	declare @PseudoCategoryId int = 11 --Cykler & Tilbehør - Cykler
	declare @RepairTreeEnabled BIT  =  1

    insert into [FT_PseudoCategoryRepairMap] ([FunctionTemplateId],[PseudoCategoryId],[RepairTreeEnabled])
    		SELECT @FunctionTemplateId, @PseudoCategoryId, @RepairTreeEnabled

	SET NOCOUNT OFF


--rollback DROP FUNCTION [dbo].[autotests_enable_pseudoCategoryRepairMap]