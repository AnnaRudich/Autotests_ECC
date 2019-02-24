--liquibase formatted sql
--changeset aru:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_map_service_agreement]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_map_service_agreement]
GO


CREATE PROCEDURE [dbo].[autotests_map_service_agreement]
        @AgreementName VARCHAR(50),
		@targetInsCompanyCode VARCHAR(50)
AS
	SET NOCOUNT ON

	declare @targetInsCompanyId int = (select ICRFNBR from InsComp where CompanyCode = @targetInsCompanyCode)
	declare @AgreementId VARCHAR(50) = (SELECT [id] FROM [ServiceAgreement] where name = @AgreementName)
	declare @maxExistingId int =  (SELECT top 1 [Id] FROM [PseudocatAgreements] order by Id desc)

	--map IC to the agreement so agreement is available in RnV wizard

    insert into [PseudocatAgreements] ([PseudoCategoryId],[ServiceAgreementId],[insuranceCompanyId],[templateId])
    		SELECT [PseudoCategoryID], @AgreementId, @targetInsCompanyId, '' FROM [PsuedoCategory] where Published = 1

	SET NOCOUNT OFF


--rollback DROP FUNCTION [dbo].[autotests_map_service_agreement]