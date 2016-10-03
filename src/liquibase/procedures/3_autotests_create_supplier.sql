--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_supplier]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
  DROP PROCEDURE [dbo].[autotests_create_supplier]
GO

CREATE PROCEDURE [dbo].[autotests_create_supplier]
    @SUNAME NVARCHAR(50),
    @SUEMAIL NVARCHAR(50),
	  @insCompanyId BIGINT
AS

SET NOCOUNT ON

DECLARE @SUCVRNBR NVARCHAR(20) = (SELECT CAST(CAST(10000000 + floor(10000000 * RAND(convert(VARBINARY, newid()))) AS INT) AS VARCHAR))
DECLARE @SUPHONE NVARCHAR(20) = '+4588818001'
DECLARE @SUADDR NVARCHAR(50) = '489-499 Avebury Boulevard'
DECLARE @SUORNOT CHAR(1) = 'M'
DECLARE @SUSUPDELIV BIT = 1
DECLARE @SUSUPPICKUP BIT = 1
DECLARE @SULOGO NVARCHAR(254) = ''
DECLARE @SUURL NVARCHAR(254) = ''
DECLARE @SUADDR2 NVARCHAR(50) = 'Milton Keynes, MK9 2NW'
DECLARE @SUVCPICT NVARCHAR(254) = ''
DECLARE @suDeliveryTime INT = 7
DECLARE @suCulture INT = (SELECT LCID FROM Culture c JOIN FormattingProperties f ON c.Culture = f.VALUE WHERE f.[KEY] = 'scalepoint_culture_code')
DECLARE @SUFREIGHTPRICE BIT = 1
DECLARE @SUORDERRETURNADDRESS NVARCHAR(200) = ''
DECLARE @SUOPENINGHOURS NVARCHAR(50) = ''
DECLARE @SUVOUCHERSONLY BIT = 0
DECLARE @PostalCode NVARCHAR(20) = 'AB12 3CD'
DECLARE @City NVARCHAR(30) = 'City'
DECLARE @LogoFileId INT = NULL
DECLARE @BannerFileId INT = NULL
DECLARE @SUVENDORACCTNO BIGINT = NULL
DECLARE @suHandlesWeee TINYINT = NULL

IF EXISTS(SELECT * FROM dbo.SUPPLIER s WHERE s.SUNAME = @SUNAME)
BEGIN
	RAISERROR ('Supplier name "%s" is already exists', 18, - 1, @SUNAME)
    RETURN
END

WHILE EXISTS(SELECT * FROM dbo.SUPPLIER WHERE SUCVRNBR = @SUCVRNBR)
BEGIN
  SET @SUCVRNBR = (SELECT CAST(CAST(10000000 + floor(10000000 * RAND(convert(VARBINARY, newid()))) AS INT) AS VARCHAR))
  CONTINUE
END

DECLARE @mainInsuranceCompanyId INT = (SELECT COALESCE(ic.ICPRFNBR, ic.ICRFNBR) FROM dbo.INSCOMP ic WHERE ic.ICRFNBR = @insCompanyId )

INSERT INTO [SUPPLIER] ([SUNAME]
  , [SUEMAIL]
  , [SUCVRNBR]
  , [SUPHONE]
  , [SUADDR]
  , [SUORNOT]
  , [SUSUPDELIV]
  , [SUSUPPICKUP]
  , [SULOGO]
  , [SUURL]
  , [SUADDR2]
  , [SUVCPICT]
  , [suDeliveryTime]
  , [suHandlesWeee]
  , [suCulture]
  , [SUFREIGHTPRICE]
  , [SUORDERRETURNADDRESS]
  , [SUOPENINGHOURS]
  , [SUVOUCHERSONLY]
  , [PostalCode]
  , [City]
  , [insuranceCompanyId]
  , [LogoFileId]
  , [BannerFileId]
  , [SUVENDORACCTNO])
  VALUES
  (@SUNAME,
   @SUEMAIL,
   @SUCVRNBR,
   @SUPHONE,
   @SUADDR,
   @SUORNOT,
   @SUSUPDELIV,
   @SUSUPPICKUP,
   @SULOGO,
   @SUURL,
   @SUADDR2,
   @SUVCPICT,
   @suDeliveryTime,
   @suHandlesWeee,
   @suCulture,
   @SUFREIGHTPRICE,
   @SUORDERRETURNADDRESS,
   @SUOPENINGHOURS,
   @SUVOUCHERSONLY,
   @PostalCode,
   @City,
   @mainInsuranceCompanyId,
   @LogoFileId,
   @BannerFileId,
   @SUVENDORACCTNO)

	PRINT 'Supplier was successfully created with id = ' + CAST(SCOPE_IDENTITY() AS VARCHAR)

SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_supplier]