--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_service_agreements]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_create_service_agreements]
GO

CREATE PROCEDURE [dbo].[autotests_create_service_agreements]
		@insCompanyId int,
		@baseTemplateName VARCHAR(50),
		@serviceAgreementName VARCHAR(50),
		@serviceAgreementNameForWizard VARCHAR(50)
AS

	SET NOCOUNT ON

	BEGIN TRANSACTION

	/* INSURANCE COMPANY */
	declare @ScalepointCompanyID int = (select ICRFNBR from InsComp where CompanyCode = 'SCALEPOINT')
	declare @ServiceAgreementTemplateID int = (Select id from ServiceAgreementTemplate where name = @baseTemplateName)
	declare @TargetCompanyName nvarchar(50) = (SELECT [ICNAME] FROM [INSCOMP] where [ICRFNBR] = @insCompanyId)

	/* SUPPLIER DATA */
	declare @SupplierName varchar(100) = CONCAT('Autotest-SA', '-', @insCompanyId )
	declare @Address1 varchar(100) = 'Test address 1'
	declare @Address2 varchar(100) = 'Test address 2'
	declare @Phone varchar(100) = '0800 0833113'
	declare @Email varchar(100) = 'ecc_auto@scalepoint.com'
	declare @PostalCode varchar(100) = '5000'
	declare @City varchar(100) = 'Test city'

	/*---------------------------------------------------------*/
	/* DELETE SUPPLIER, SERVICE AGREEMENT, LOCATION, MAPPINGS */
	declare @DeleteSupplierID int = (select top 1 SURFNBR from [SUPPLIER] where [SUNAME] = @SupplierName)
	IF (@DeleteSupplierID IS NOT NULL)
		RETURN

  declare @SupplierId int;
  execute autotests_create_supplier @SupplierName, @Email, @insCompanyId, @PostalCode, @SupplierId OUTPUT;

	declare @AgreementTags varchar(100) = ''
	declare @AgreementStatus bit = 1 -- 1 = Active, 0 = Inactive
	declare @AgreementEmail varchar (1) = 'S' -- S = Supplier, A = Agreement, L = Location

	/* SERVICE AGREEMENT */
	insert into [ServiceAgreement] ([name],[email],[tags],[status],[emailType],[supplierId],[insuranceCompanyId],[defaultTemplateId],[notes],[workflow],[reminder],[timeout])
		select @serviceAgreementName,@Email,@AgreementTags,@AgreementStatus,@AgreementEmail,@SupplierID,@insCompanyId,@ServiceAgreementTemplateID,'','',NULL,NULL
	declare @AgreementId int = @@IDENTITY

	insert into [PseudocatAgreements] ([PseudoCategoryId],[ServiceAgreementId],[insuranceCompanyId],templateId)
		SELECT [PseudoCategoryID], @AgreementId, @insCompanyId, '' FROM [PsuedoCategory] where Published = 1

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
		select @serviceAgreementNameForWizard,@Email,@AgreementTags,@AgreementStatus,@AgreementEmail,@SupplierID,@insCompanyId,@ServiceAgreementTemplateID,'','',NULL,NULL

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
	declare @ShopName varchar(100) = CONCAT('Test shop ', @TargetCompanyName)
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

	COMMIT TRANSACTION

	SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_service_agreements]