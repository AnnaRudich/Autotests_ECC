--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_ic]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
  DROP PROCEDURE [dbo].[autotests_create_ic]
GO

CREATE PROCEDURE [dbo].[autotests_create_ic]
	  @ICRFNBR int,
    @ICNAME nvarchar(254),
	  @ICCOMMAIL nvarchar(254) = 'ecc_auto@scalepoint.com',
    @ICPRFNBR bigint = NULL,
	  @icRecieveSelfServiceNotification BIT = 1,
	  @icSendSelfserviceNotificationTo INT = 0,
	  @icRecieveClaimNotification BIT = 1,
	  @icSendClaimNotificationTo INT = 0,
	  @icRecieveExcelImportNotification BIT = 1,
	  @icSendExcelImportNotificationTo INT = 0,
	  @icRecieveNoteNotification BIT =  1,
	  @icSendNoteNotificationTo INT =  0,
	  @icRecieveSPNotification BIT = 1,
	  @icSendSPNotificationTo INT = 0,
	  @icEmailAddressInCustomerMail INT = 0,

	  @icAuditEnabled INT = 1,
	  @icEventAPIEnabled INT = 1,
	  @icAuditAllowAutoComplete INT = 1,
	  @auditButtonEnabled INT = 1,
	  @auditAllowValidateRV INT = 1
AS

SET NOCOUNT ON

DECLARE @CompanyCode nvarchar(50) = UPPER(@ICNAME)
DECLARE @ICZIPC nvarchar(10) = 'BS1 4DJ2'
DECLARE @ICCITY nvarchar(50)= 'Bristol2'
DECLARE @ICURL nvarchar(254)= 'http://www.scalepoint.com2'
DECLARE @ICGTNBR INT = 1
DECLARE @icCulture INT =  (SELECT LCID FROM Culture c JOIN FormattingProperties f ON c.Culture = f.VALUE WHERE f.[KEY] = 'scalepoint_culture_code')
DECLARE @ICLOGO nvarchar(254) = NULL
DECLARE @ICADDR1 nvarchar(50) = '57 Ladymead'
DECLARE @ICADDR2 nvarchar(50) = 'Surrey'
DECLARE @icInsuranceCompanyToken uniqueidentifier = NEWID()
DECLARE @ICSTATECODE nvarchar(2) = NULL
DECLARE @icNewShopLogo nvarchar(256) = NULL
DECLARE @IcAllowCreateOwn BIT = 1
DECLARE @SMSDISPLAYNAME VARCHAR(11) = ''
DECLARE @icFlagOverride INT  = 0
DECLARE @ICFTNBR INT = @ICRFNBR

IF EXISTS(SELECT * FROM dbo.INSCOMP ic WHERE ic.ICNAME = @ICNAME) OR EXISTS(SELECT * FROM dbo.INSCOMP ic WHERE ic.ICRFNBR = @ICRFNBR)
    RETURN

	DECLARE @icTextId INT = (SELECT MAX([TextId])+1 FROM dbo.Text)
	SET IDENTITY_INSERT [Text] ON
	INSERT INTO [Text] (TextId, TextType) values (@icTextId, 11)
	SET IDENTITY_INSERT [Text] OFF
	INSERT INTO [Text_InsComp] ([TextId], [LCID], [ContactNo], [OfficeHours], [LocalizedName]) VALUES (@icTextId, @icCulture, '0844 391 4086', 'no opening hours', @ICNAME)

	DECLARE @departmentId UNIQUEIDENTIFIER = NEWID()
	INSERT INTO Department (DepartmentToken, Parent, Description, Active, Email) VALUES (@departmentId, NULL, @ICNAME, 1, @ICCOMMAIL)
	INSERT INTO Department (DepartmentToken, Parent, Description, Active, Email) VALUES (NEWID(), @departmentId, @ICNAME+' Department', 1, @ICCOMMAIL)

	DECLARE @ftExists BIT = 1

	DECLARE @scalepointId int = (SELECT [ICRFNBR] FROM [INSCOMP] where CompanyCode = 'SCALEPOINT')
	DECLARE @scalepointFtId int = (SELECT FTRFNBR FROM FUNCTEMPLATE WHERE FTTMPLNAME = 'Default')
	IF (@ICFTNBR IS NULL)
	  SET @ICFTNBR = @scalepointFtId
	ELSE
		SET @ftExists = (SELECT count(*) FROM FUNCTEMPLATE where FTRFNBR = @ICFTNBR)

	IF (@ftExists = 0)
  BEGIN

    declare @textId bigint = (SELECT max([TextId])+1 FROM [Text])

    SET IDENTITY_INSERT [Text] ON
    INSERT INTO [Text] ([TextId], [TextType]) VALUES (@textId, 12)
    SET IDENTITY_INSERT [Text] OFF

    INSERT INTO [Text_FunctionTemplate]
             ([TextId]
             ,[LCID]
             ,[NewPriceText]
             ,[CustomerDemandText]
             ,[PurchasePriceText]
             ,[AcquiredColumnText]
             ,[AcquiredNewItemText]
             ,[AcquiredUsedItemText]
             ,[AcquiredHeritageItemText]
             ,[AcquiredGiftItemText]
             ,[RoomText])
      SELECT @textId
        ,[LCID]
        ,[NewPriceText]
        ,[CustomerDemandText]
        ,[PurchasePriceText]
        ,[AcquiredColumnText]
        ,[AcquiredNewItemText]
        ,[AcquiredUsedItemText]
        ,[AcquiredHeritageItemText]
        ,[AcquiredGiftItemText]
        ,[RoomText]
      FROM [Text_FunctionTemplate]
      WHERE TextId = (SELECT ftTextId FROM FUNCTEMPLATE WHERE FTRFNBR = @scalepointFtId)

      SET IDENTITY_INSERT [FUNCTEMPLATE] ON
      INSERT INTO [dbo].[FUNCTEMPLATE]
         ([FTRFNBR]
         ,[FTREDUCFLAGS]
         ,[FTSHOPMENUFLAGS]
         ,[FTZIPLENGTH]
         ,[FTNOFLAGS]
         ,[FTFUNCTIONFLAGS]
         ,[FTREGNRLENGTH]
         ,[FTACCOUNTNBRFLAGS]
         ,[FTTRADEUPMULTIPLIER]
         ,[FTZIPCODECHECKFLAGS]
         ,[ftmailflags]
         ,[FTDEFDEDUC]
         ,[FTVATPRC]
         ,[FTTMPLNAME]
         ,[fttradeupadd]
         ,[ftlogindays]
         ,[FTSELECTICFLAGS]
         ,[fttradeupmax]
         ,[ftchequeprice]
         ,[ftpaymentflags]
         ,[ftdefaultcanceldelay]
         ,[ftfunctionflags2]
         ,[ftanswerminvalue]
         ,[ftanswermaxvalue]
         ,[fttaxmodel]
         ,[ftcatalogflags]
         ,[ftsessioninactive]
         ,[ftQuestionnaire]
         ,[ftCancelReminderDelay]
         ,[ftfunctionflags3]
         ,[ftStripText]
         ,[ftvoucherflags]
         ,[ftemcflags]
         ,[ftscreenwidth]
         ,[ftscreenheight]
         ,[ftCategorySelection]
         ,[ftPayoutSendMail]
         ,[ftSuggestFlags]
         ,[ftintegrationflags]
         ,[ftnewshopmenuflags]
         ,[FTNOSELFLAGS]
         ,[ftnumberbestfitresults]
         ,[ftMaxZipCodeDistance]
         ,[ftfunctionflags4]
         ,[ftTextId]
         ,[ftSelfServiceFlags]
         ,[PseudoCatModelId]
         ,[ftPwdExpDays]
         ,[ftMaxNotificationDays]
         ,[ftfunctionflags5]
         ,[ftUserPasswordValidationStrategy]
         ,[savedPwdsCount]
         ,[maxLoginAttempts]
         ,[ftDnD2Related]
         ,[AUTO_REIMBURSE_FLAG]
         ,[AUTO_REIMBURSE_EXPIRATION_UNIT]
         ,[AUTO_REIMBURSE_EXPIRATION_VALUE]
         ,[AUTO_REIMBURSE_REFERENCE]
         ,[ftForcePayoutAmount]
         ,[requiredValForDiscrVal]
         ,[ftDefaultInvoicePayer]
         ,[hide_customer_demand_for_claimant]
         ,[hide_voucher_face_value_for_claimant]
         ,[ftAutoApprovalFlag]
         ,[cwaDuePeriod])
      SELECT @ICRFNBR
          ,[FTREDUCFLAGS]
          ,[FTSHOPMENUFLAGS]
          ,[FTZIPLENGTH]
          ,[FTNOFLAGS]
          ,[FTFUNCTIONFLAGS]
          ,[FTREGNRLENGTH]
          ,[FTACCOUNTNBRFLAGS]
          ,[FTTRADEUPMULTIPLIER]
          ,[FTZIPCODECHECKFLAGS]
          ,[ftmailflags]
          ,[FTDEFDEDUC]
          ,[FTVATPRC]
          ,@ICNAME
          ,[fttradeupadd]
          ,[ftlogindays]
          ,[FTSELECTICFLAGS]
          ,[fttradeupmax]
          ,[ftchequeprice]
          ,[ftpaymentflags]
          ,[ftdefaultcanceldelay]
          ,[ftfunctionflags2]
          ,[ftanswerminvalue]
          ,[ftanswermaxvalue]
          ,[fttaxmodel]
          ,[ftcatalogflags]
          ,[ftsessioninactive]
          ,[ftQuestionnaire]
          ,[ftCancelReminderDelay]
          ,[ftfunctionflags3]
          ,[ftStripText]
          ,[ftvoucherflags]
          ,[ftemcflags]
          ,[ftscreenwidth]
          ,[ftscreenheight]
          ,[ftCategorySelection]
          ,[ftPayoutSendMail]
          ,[ftSuggestFlags]
          ,[ftintegrationflags]
          ,[ftnewshopmenuflags]
          ,[FTNOSELFLAGS]
          ,[ftnumberbestfitresults]
          ,[ftMaxZipCodeDistance]
          ,[ftfunctionflags4]
          ,@textId
          ,[ftSelfServiceFlags] | power(2,17)
          ,[PseudoCatModelId]
          ,[ftPwdExpDays]
          ,[ftMaxNotificationDays]
          ,[ftfunctionflags5]
          ,[ftUserPasswordValidationStrategy]
          ,[savedPwdsCount]
          ,[maxLoginAttempts]
          ,[ftDnD2Related]
          ,[AUTO_REIMBURSE_FLAG]
          ,[AUTO_REIMBURSE_EXPIRATION_UNIT]
          ,[AUTO_REIMBURSE_EXPIRATION_VALUE]
          ,[AUTO_REIMBURSE_REFERENCE]
          ,[ftForcePayoutAmount]
          ,[requiredValForDiscrVal]
          ,[ftDefaultInvoicePayer]
          ,[hide_customer_demand_for_claimant]
          ,[hide_voucher_face_value_for_claimant]
          ,[ftAutoApprovalFlag]
          ,[cwaDuePeriod]
        FROM [FUNCTEMPLATE] where FTRFNBR = @scalepointFtId
        SET IDENTITY_INSERT [FUNCTEMPLATE] OFF
	END

INSERT INTO [INSCOMP]
       ([ICRFNBR]
       ,[ICNAME]
       ,[ICLOGO]
       ,[ICADDR1]
       ,[ICADDR2]
       ,[ICZIPC]
       ,[ICCITY]
       ,[ICURL]
       ,[ICCOMMAIL]
       ,[ICGTNBR]
       ,[ICFTNBR]
       ,[ICPRFNBR]
       ,[CompanyCode]
       ,[icInsuranceCompanyToken]
       ,[ICSTATECODE]
       ,[icDepartment]
       ,[icCulture]
       ,[icNewShopLogo]
       ,[IcAllowCreateOwn]
       ,[icRecieveSPNotification]
       ,[icSendSPNotificationTo]
       ,[SMSDISPLAYNAME]
       ,[icTextId]
       ,[icFlagOverride]
       ,[icSendSelfserviceNotificationTo]
       ,[icRecieveSelfServiceNotification]
       ,[icRecieveClaimNotification]
       ,[icSendClaimNotificationTo]
       ,[icRecieveExcelImportNotification]
       ,[icSendExcelImportNotificationTo]
       ,[icRecieveNoteNotification]
       ,[icSendNoteNotificationTo]
       ,[icEmailAddressInCustomerMail]
       ,[KvitteringStoreName]
       ,[icAllowShareData3rdParty]
       ,[icTrustpilotMail]
       ,[xPriceModelId]
       ,[icNewShopUrlSpecifier]
       ,[icIntegrateNotification]
       ,[isOrderFeeEnabled]
       ,[Tenant]
       ,[UnifiedCompanyCode]
       ,[selfriskByInsuranceCompany]
       ,[invoicePaymentByInsuranceCompany]
       ,[reminderDays]
       ,[autoApproveDays]
       ,[icReceiveCancelClaimNotification]
       ,[auditCompanyCode]
       ,[auditEnabled]
       ,[eventAPIEnabled]
       ,[auditAllowAutoComplete]
       ,[auditButtonEnabled]
       ,[auditAllowValidateRV]
       ,[omTenantAlias]
       ,[omCompanyAlias]
       ,[cwaTenant])
   VALUES
       (@ICRFNBR,@ICNAME,@ICLOGO,@ICADDR1,@ICADDR2,@ICZIPC ,@ICCITY,@ICURL,@ICCOMMAIL,@ICGTNBR,@ICRFNBR,@ICPRFNBR,
       @CompanyCode,@icInsuranceCompanyToken,@ICSTATECODE,@departmentId,@icCulture,@icNewShopLogo,@IcAllowCreateOwn
       ,@icRecieveSPNotification
       ,@icSendSPNotificationTo
       ,@SMSDISPLAYNAME
       ,@icTextId
       ,@icFlagOverride
       ,@icSendSelfserviceNotificationTo
       ,@icRecieveSelfServiceNotification
       ,@icRecieveClaimNotification
       ,@icSendClaimNotificationTo
       ,@icRecieveExcelImportNotification
       ,@icSendExcelImportNotificationTo
       ,@icRecieveNoteNotification
       ,@icSendNoteNotificationTo
       ,@icEmailAddressInCustomerMail
		   ,@ICNAME
		   ,0
		   ,NULL
		   ,1
		   ,lower(@ICNAME)
		   ,0
		   ,0
		   ,lower(@ICNAME)
		   ,lower(@ICNAME)
		   ,1
		   ,0
		   ,5
		   ,2
		   ,1
		   ,@ICNAME
		   ,@icAuditEnabled
           ,@icEventAPIEnabled
           ,@icAuditAllowAutoComplete
           ,@auditButtonEnabled
           ,@auditAllowValidateRV
           ,lower(@ICNAME)
           ,lower(@ICNAME)
           ,lower(@ICNAME))

INSERT INTO [PseudocatVouchers] ([PseudoCategoryId], [VoucherAgreementId], [insuranceCompanyId])
	  SELECT [PseudoCategoryId], [VoucherAgreementId], @ICRFNBR FROM [PseudocatVouchers] where insuranceCompanyId = @scalepointId

    PRINT 'Insurance company was successfully created with id = ' + CAST(@ICRFNBR AS VARCHAR)

INSERT INTO [InsuranceCompanyCalendar]([insuranceCompanyId]
      ,[workingDayMonday]
      ,[workingDayTuesday]
      ,[workingDayWednesday]
      ,[workingDayThursday]
      ,[workingDayFriday]
      ,[workingDaySaturday]
      ,[workingDaySunday]
      ,[automaticMailStartTime]
      ,[automaticMailEndTime]
      ,[automaticMailDelayFrom]
      ,[automaticMailDelayTo])
VALUES (@ICRFNBR,
      1,
      1,
      1,
      1,
      1,
      1,
      1,
      '01:00:00',
      '23:00:00',
      0,
        0)

SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_ic]