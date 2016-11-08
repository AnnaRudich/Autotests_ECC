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
	declare @SupplierName varchar(100) = CONCAT('ATSupplier-', @serviceAgreementName, '-', @insCompanyId )
	declare @SupplierCVR varchar(100) = '123456789'
	declare @Email varchar(100) = 'ecc_auto@scalepoint.com'
	declare @Phone varchar(100) = '0800 0833113'
	declare @Address1 varchar(100) = 'Test address 1'
	declare @Address2 varchar(100) = 'Test address 2'
	declare @PostalCode varchar(100) = 'AB10 123'
	declare @City varchar(100) = 'Test city'
	declare @SupplierURL varchar(100) = 'http://www.scalepoint.com'

	/*---------------------------------------------------------*/
	/* DELETE SUPPLIER, SERVICE AGREEMENT, LOCATION, MAPPINGS */
	declare @DeleteSupplierID int = (select top 1 SURFNBR from [SUPPLIER] where [SUNAME] = @SupplierName)
	IF (@DeleteSupplierID IS NOT NULL)
		RETURN


	--delete from [Location_ZipCode] where locationID in (select LocationID from Location where [shopId] in (select PURFNBR from PICKUP where PUSUNBR = @DeleteSupplierID))
	--delete from [Location] where [shopId] in (select PURFNBR from PICKUP where PUSUNBR = @DeleteSupplierID)
	--delete from PICKUP where PUSUNBR = @DeleteSupplierID
	--delete from [ServiceAgreementTaskTypeMap] where ServiceAgreementId in (select ID from [ServiceAgreement] where supplierId = @DeleteSupplierID)
	--delete from [ServiceAgreement] where supplierId = @DeleteSupplierID
	--delete from [SUPPLIER] where SURFNBR = @DeleteSupplierID

	insert into [SUPPLIER] (
		[SUNAME],[SUEMAIL],[SUCVRNBR],[SUPHONE],[SUADDR],[SUADDR2],[PostalCode],[City],[SUURL],
		[insuranceCompanyId],[SUORNOT],	[SUSUPDELIV],	[SUSUPPICKUP],	[SULOGO],[SUVCPICT],[suDeliveryTime],[suHandlesWeee],[suCulture],[SUFREIGHTPRICE],[SUORDERRETURNADDRESS],[SUOPENINGHOURS],[SUVOUCHERSONLY],[LogoFileId],[BannerFileId],[SUVENDORACCTNO],[PushOrderDocument],[rvIntegrationType])
		SELECT
			@SupplierName,@Email,@SupplierCVR,@Phone,@Address1,@Address2,@PostalCode,@City,@SupplierURL,
			@insCompanyId, 'M','1','0','\jessops_logo.gif','\jessops_logo.gif','7',NULL,'2057','1','','','0','14','13',NULL,'0', 'XL'
	declare @SupplierID int = @@IDENTITY
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


	/*---------------------------------------------------------*/
	/* SHOP DATA */
	declare @ShopName varchar(100) = CONCAT('Test shop ', @TargetCompanyName)
	declare @ShopName2 varchar(100) = @ShopName+' 2'
	declare @IsRetailShop bit = 1
	declare @IsRepairValuationLocation bit = 1

	/*SUPPLIER SHOP #1 - R&V LOCATION #1*/
	insert into PICKUP (PUSUNBR,PUNAME,PUZIPC,PUADDR1,PUADDR2,PUCITY,PUPHONE,puSearchZip,puRetailShop,puRepairValuationLocation, updatedDate)
		select @SupplierID, @ShopName, @PostalCode, @Address1, @Address2, @City, @Phone, substring(@PostalCode, 1, charindex(' ',@PostalCode)), @IsRetailShop, @IsRepairValuationLocation, GETDATE()
	declare @PickupID int = @@IDENTITY

	insert into [Location] ([shopId], [serviceAgreementId],[contactEmail]) select @PickupID, @AgreementId, @Email
	declare @LocationId int = @@IDENTITY

	insert into [Location_ZipCode] ([locationId],[zipCodeId]) select distinct @LocationId, ZIPCODE from ZipCodes

	/*SUPPLIER SHOP #2 - R&V LOCATION #2*/
	insert into PICKUP (PUSUNBR,PUNAME,PUZIPC,PUADDR1,PUADDR2,PUCITY,PUPHONE,puSearchZip,puRetailShop,puRepairValuationLocation, updatedDate)
		select @SupplierID,@ShopName2,@PostalCode,@Address1,@Address2,@City,@Phone,substring(@PostalCode, 1, charindex(' ',@PostalCode)),@IsRetailShop,@IsRepairValuationLocation, GETDATE()
	declare @PickupId2 int = @@IDENTITY

	insert into [Location] ([shopId], [serviceAgreementId],[contactEmail]) select @PickupId2, @AgreementIdForWizard, @Email
	declare @LocationId2 int = @@IDENTITY

	insert into [Location_ZipCode] ([locationId],[zipCodeId]) select distinct @LocationId2,ZIPCODE from ZipCodes



	COMMIT TRANSACTION

	SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_service_agreements]