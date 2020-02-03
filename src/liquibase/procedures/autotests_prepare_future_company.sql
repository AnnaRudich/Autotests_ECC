--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_prepare_future_company]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
  DROP PROCEDURE [dbo].[autotests_prepare_future_company]
GO

CREATE PROCEDURE [dbo].[autotests_prepare_future_company]
    @companyId int,
    @userRights int,
    @MockUrl varchar(50)
AS

SET NOCOUNT ON

    declare @masterTemplateName varchar(50) = 'mastr'
    declare @serviceAgreementName varchar(50) = 'AutoTest Agreement'
    declare @serviceAgreementNameForWizard varchar(50) = 'AutoTest Agreement For Wizard'
    declare @supplierName varchar(50) = concat('AutotestSupplier-', @companyId, '-ForVaTests')

    declare @companyName nvarchar(254) = concat('Future', @companyId)
    declare @userName nvarchar(254) = concat('autotest-future', @companyId)

    EXEC autotests_create_ic @companyId, @companyName
    EXEC autotests_create_user @companyId, @userName, @userRights
    EXEC autotests_create_choice_reasons @companyId
    --EXEC autotests_create_service_agreements @companyId, @masterTemplateName, @serviceAgreementName, @serviceAgreementNameForWizard

    declare @SupplierId int
    EXEC autotests_create_supplier @SUNAME = @supplierName, @insCompanyId = @companyId, @PostalCode = '4321', @SupplierId = @SupplierId OUTPUT, @MockUrl = @MockUrl

SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_prepare_future_company]