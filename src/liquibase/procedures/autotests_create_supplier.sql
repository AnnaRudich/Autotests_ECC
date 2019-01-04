--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_supplier]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
  DROP PROCEDURE [dbo].[autotests_create_supplier]
GO

CREATE PROCEDURE [dbo].[autotests_create_supplier]
    @SUNAME NVARCHAR(50),
	  @insCompanyId BIGINT,
	  @PostalCode NVARCHAR(20),
	  @SecurityToken varchar(100) = null,
      @RV_TaskWebServiceUrl varchar(100) = null,
      @SecurityTokenIssued varchar(100) = null,
 	  @SupplierId int OUTPUT
     AS

SET NOCOUNT ON

DECLARE @SUCVRNBR NVARCHAR(20) = (SELECT CAST(CAST(10000000 + floor(10000000 * RAND(convert(VARBINARY, newid()))) AS INT) AS VARCHAR))
DECLARE @SUPHONE NVARCHAR(20) = '+4588818001'
DECLARE @SUADDR NVARCHAR(50) = '489-499 Avebury Boulevard'
DECLARE @SUADDR2 NVARCHAR(50) = 'Milton Keynes, MK9 2NW'
DECLARE @suCulture INT = (SELECT LCID FROM Culture c JOIN FormattingProperties f ON c.Culture = f.VALUE WHERE f.[KEY] = 'scalepoint_culture_code')
DECLARE @City NVARCHAR(30) = 'Copenhagen'

IF EXISTS(SELECT * FROM dbo.SUPPLIER s WHERE s.SUNAME = @SUNAME)
BEGIN
	PRINT FORMATMESSAGE('Supplier name "%s" is already exists', @SUNAME)
    select @SupplierId = (SELECT SURFNBR FROM dbo.SUPPLIER s WHERE s.SUNAME = @SUNAME)
	RETURN
END

WHILE EXISTS(SELECT * FROM dbo.SUPPLIER WHERE SUCVRNBR = @SUCVRNBR)
BEGIN
  SET @SUCVRNBR = (SELECT CAST(CAST(10000000 + floor(10000000 * RAND(convert(VARBINARY, newid()))) AS INT) AS VARCHAR))
  CONTINUE
END

DECLARE @mainInsuranceCompanyId INT = (SELECT COALESCE(ic.ICPRFNBR, ic.ICRFNBR) FROM dbo.INSCOMP ic WHERE ic.ICRFNBR = @insCompanyId )

insert into [SUPPLIER] (
		[SUNAME],  [SUEMAIL], [SUCVRNBR], [SUPHONE], [SUADDR],
		[SUADDR2], [PostalCode],[City], [SUURL], [insuranceCompanyId],
		[SUORNOT], [SUSUPDELIV], [SUSUPPICKUP],	[SULOGO],[SUVCPICT],
		[suDeliveryTime], [suHandlesWeee], [suCulture], [SUFREIGHTPRICE], [SUORDERRETURNADDRESS],
		[SUOPENINGHOURS], [SUVOUCHERSONLY], [LogoFileId], [BannerFileId],[SUVENDORACCTNO],
		[PushOrderDocument], [RV_TaskWebServiceUrl], [securityToken], [securityTokenIssued], [rvIntegrationType])
		SELECT
			@SUNAME, 'ecc_auto@scalepoint.com', @SUCVRNBR, @SUPHONE, @SUADDR,
			@SUADDR2, @PostalCode, @City, '', @insCompanyId,
			'M',     1,           1,     '\jessops_logo.gif', '\jessops_logo.gif',
			7,       NULL,        @suCulture,  1,       '',
			'',     0,            NULL,   NULL,   NULL,
			0, @RV_TaskWebServiceUrl, @SecurityToken, @SecurityTokenIssued, 3

	select @SupplierID = @@IDENTITY

	RETURN

SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_supplier]