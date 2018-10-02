--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_service_agreements]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_create_service_agreements]
GO


CREATE PROCEDURE [dbo].[autotests_create_service_agreements]
		@insCompanyId int,
		@serviceAgreementName VARCHAR(50),
		@serviceAgreementNameForWizard VARCHAR(50)
AS

	SET NOCOUNT ON

	/* INSURANCE COMPANY */
	declare @ScalepointCompanyID int = (select ICRFNBR from InsComp where CompanyCode = 'SCALEPOINT')
	declare @ServiceAgreementTemplateID int = (Select top 1 id from ServiceAgreementTemplate order by id desc)
	declare @targetInsCompanyId int = (select ICRFNBR from InsComp where CompanyCode = 'TOPDANMARK') --SCALEPOINT is default, won't work

	/* SUPPLIER DATA */
	declare @SupplierName varchar(100) = 'Autotest-Supplier-RnV-Tests'
	declare @Address1 varchar(100) = 'Test address 1'
	declare @Address2 varchar(100) = 'Test address 2'
	declare @Phone varchar(100) = '0800 0833113'
    declare @Email varchar(100) = 'ecc_auto@scalepoint.com'
	declare @PostalCode varchar(100) = '4321'
	declare @City varchar(100) = 'Test city'
	declare @SecurityToken varchar(100) = '589356A5-2E39-4438-B45F-8E05C545ABD2'
	declare @RV_TaskWebServiceUrl varchar(100) = 'http://httpbin.org/post'
    declare @SecurityTokenIssued varchar(100) = '2018-06-05 00:00:00.000'

	/*---------------------------------------------------------*/
	/* SUPPLIER RnV*/
	--If supplier is already exists use it
	declare @ExistingSupplierID int = (select top 1 SURFNBR from [SUPPLIER] where [SUNAME] = @SupplierName)
	declare @SupplierId int

	IF(@ExistingSupplierID IS NOT NULL)
	BEGIN
		SET @SupplierId = @ExistingSupplierID
	END
    --add supplier if there is no
	IF (@ExistingSupplierID IS NULL)
	BEGIN
		execute autotests_create_supplier @SupplierName, @insCompanyId, @PostalCode,@SecurityToken, @RV_TaskWebServiceUrl, @SecurityTokenIssued, @SupplierId OUTPUT;
	END

    /* SERVICE AGREEMENT */
	declare @AgreementTags varchar(100) = ''
	declare @AgreementStatus bit = 1 -- 1 = Active, 0 = Inactive
	declare @AgreementEmailType varchar (1) = 'S' -- S = Supplier, A = Agreement, L = Location

    --use but not add if already exists
    IF EXISTS(SELECT * FROM [ServiceAgreement] WHERE [name]=@serviceAgreementName)
    BEGIN
        PRINT FORMATMESSAGE('ServiceAgreement name "%s" is already exists', @serviceAgreementName)
        SELECT @AgreementId = (SELECT id FROM [ServiceAgreement] WHERE [name]=@serviceAgreementName)
        RETURN
    END

    --add service agreement
	insert into [ServiceAgreement] (
	[name],[email],[tags],[status],[emailType],[supplierId],
	[insuranceCompanyId],[defaultTemplateId],[notes],[workflow],[reminder],[timeout])
		select @serviceAgreementName,@Email,@AgreementTags,@AgreementStatus,
		@AgreementEmailType,@SupplierID,@targetInsCompanyId,@ServiceAgreementTemplateID,'','',NULL,NULL
	declare @AgreementId int = @@IDENTITY

	insert into [PseudocatAgreements] ([PseudoCategoryId],[ServiceAgreementId],[insuranceCompanyId],templateId)
		SELECT [PseudoCategoryID], @AgreementId, @targetInsCompanyId, '' FROM [PsuedoCategory] where Published = 1

	insert into [ServiceAgreementTaskTypeMap] (ServiceAgreementId,TaskTypeId)
		select @AgreementId, 1
		union all
		select @AgreementId, 2
		union all
		select @AgreementId, 3
		union all
		select @AgreementId, 4

	/* SERVICE AGREEMENT FOR WIZARD */
	INSERT INTO [ServiceAgreement] ([name], [email], [tags], [status], [emailType], [supplierId], [insuranceCompanyId], [defaultTemplateId], [notes], [workflow], [reminder], [timeout])
		select @serviceAgreementNameForWizard,@Email,@AgreementTags,@AgreementStatus,@AgreementEmailType,@SupplierID,@insCompanyId,@ServiceAgreementTemplateID,'','',NULL,NULL

	declare @AgreementIdForWizard int = @@IDENTITY
	insert into [PseudocatAgreements] ([PseudoCategoryId],[ServiceAgreementId],[insuranceCompanyId],templateId)
		SELECT [PseudoCategoryID], @AgreementIdForWizard, @insCompanyId,'' FROM [PsuedoCategory] where Published = 1

	insert into [ServiceAgreementTaskTypeMap] (ServiceAgreementId,TaskTypeId)
		select @AgreementIdForWizard, 1
		union all
		select @AgreementIdForWizard, 2
		union all
		select @AgreementIdForWizard, 3
		union all
		select @AgreementIdForWizard, 4

	/*---------------------------------------------------------*/
	/* SHOP DATA */
	declare @ShopName varchar(100) = CONCAT('Test shop ', @ScalepointCompanyId)
	declare @ShopName2 varchar(100) = @ShopName+' 2'
	declare @IsRetailShop bit = 1
	declare @IsRepairValuationLocation bit = 1

	/*SUPPLIER SHOP #1 - R&V LOCATION #1*/
	declare @PickupId int
	exec autotests_create_shop @ShopName, @SupplierID, @PostalCode, @IsRetailShop, @IsRepairValuationLocation, @PickupId OUTPUT

	insert into [Location] ([shopId], [serviceAgreementId],[contactEmail]) select @PickupID, @AgreementId, @Email
	declare @LocationId int = @@IDENTITY

	insert into [Location_ZipCode] ([locationId],[zipCodeId]) select distinct @LocationId, ZIPCODE from ZipCodes

	/*SUPPLIER SHOP #2 - R&V LOCATION #2*/
	declare @PickupId2 int
	exec autotests_create_shop @ShopName2, @SupplierID, @PostalCode, @IsRetailShop, @IsRepairValuationLocation, @PickupId2 OUTPUT

	insert into [Location] ([shopId], [serviceAgreementId],[contactEmail]) select @PickupId2, @AgreementIdForWizard, @Email
	declare @LocationId2 int = @@IDENTITY

	insert into [Location_ZipCode] ([locationId],[zipCodeId]) select distinct @LocationId2,ZIPCODE from ZipCodes

	SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_service_agreements]