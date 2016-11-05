--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_voucher_agreement]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
  DROP PROCEDURE [dbo].[autotests_create_voucher_agreement]
GO

CREATE PROCEDURE [dbo].[autotests_create_voucher_agreement]
    @VoucherAgreementName NVARCHAR(254),
    @SupplierId INT,
    @CategoriesAsStr VARCHAR(255),
    @Discount DECIMAL(10, 8) = 20.0,
    @InsCompanyId INT = NULL,
    @MinAmount INT = 1,
    @StepAmount INT = 1,
    @EVoucherA BIT = 0,
    @Conditions ntext = 'Conditions',
    @Limitations ntext = 'Limitations'
AS

SET NOCOUNT ON

DECLARE @categories TABLE(id BIGINT NOT NULL)
DECLARE @RebatePercentage DECIMAL(10, 8) = @Discount
DECLARE @InvoicePercentage DECIMAL(10, 8) = @Discount
DECLARE @VATPercentage DECIMAL(18, 9) = 0.000000000
DECLARE @DeliveryCost DECIMAL(15, 2) = 0.00
DECLARE @LowestPrice DECIMAL(15, 2) = 1.0
DECLARE @InvoicePriceVA DECIMAL(15, 4) = 1.000
DECLARE @EmailType CHAR(1) = 'G'
DECLARE @Status BIT = 1
DECLARE @IncludeDelivery BIT = 0
DECLARE @CreationDate DATETIME = GETDATE()
DECLARE @IsSupplierUrl BIT = 1
DECLARE @IsSupplierLogo BIT = 1
DECLARE @ThumbnailImageFileId INT = 0
DECLARE @Url NVARCHAR(254) = NULL
DECLARE @Email NVARCHAR(50) = NULL
DECLARE @DeletionDate DATETIME = NULL
DECLARE @ThumbnailImage VARCHAR(254) = NULL
DECLARE @cultureId int = (select LCID from Culture where Culture = (select value from FormattingProperties where [KEY] = 'scalepoint_culture_code'))

IF (@InsCompanyId IS NULL)
  SET @InsCompanyId = (SELECT s.insuranceCompanyId FROM dbo.SUPPLIER s WHERE s.SURFNBR = @SupplierId)

DECLARE @MainInsuranceCompanyId INT = (SELECT COALESCE(ic.ICPRFNBR, ic.ICRFNBR) FROM dbo.INSCOMP ic WHERE ic.ICRFNBR = @InsCompanyId )
DECLARE @IsScalepoint BIT = (SELECT CASE WHEN CompanyCode='SCALEPOINT' THEN 1 ELSE 0 END FROM dbo.INSCOMP WHERE ICRFNBR = @MainInsuranceCompanyId)


IF NOT exists(SELECT * FROM SUPPLIER WHERE SURFNBR = @SupplierId)
BEGIN
	RAISERROR ('Supplier id "%d" is not exists', 18, - 1, @SupplierId)
    RETURN
END

IF exists(SELECT c.id FROM @categories c LEFT OUTER JOIN PsuedoCategory pc ON c.id = pc.PseudoCategoryID WHERE pc.PseudoCategoryID IS null)
BEGIN
    RAISERROR ('Pseudo categories contain not valid id', 18, - 1)
    RETURN
END

IF exists(SELECT * FROM VoucherAgreement WHERE VoucherName = @VoucherAgreementName AND SupplierId = @SupplierId)
BEGIN
    RETURN
END

IF (LEN(@CategoriesAsStr)>0)
INSERT INTO @categories SELECT * FROM autotests_splitstr_on_ints(@CategoriesAsStr)

DECLARE @PseudocatModelId INT = (
  SELECT pm.PseudoCatModelId FROM INSCOMP ic
	JOIN FUNCTEMPLATE ft ON ic.ICFTNBR = ft.FTRFNBR
    JOIN PSEUDOCAT_Model pm ON pm.PseudoCatModelId = ft.PseudoCatModelId
    WHERE ic.ICRFNBR = @MainInsuranceCompanyId)

BEGIN TRANSACTION
	INSERT INTO [VoucherAgreement] ([Status]
    , [Url]
    , [StepAmount]
    , [MinAmount]
    , [RebatePercentage]
    , [InvoicePercentage]
    , [VATPercentage]
    , [EVoucher]
    , [DeliveryCost]
    , [IncludeDelivery]
    , [VoucherName]
    , [SupplierId]
    , [ThumbnailImage]
    , [CreationDate]
    , [DeletionDate]
    , [LowestPrice]
    , [InvoicePrice]
    , [Email]
    , [EmailType]
    , [insuranceCompanyId]
    , [IsSupplierUrl]
    , [IsSupplierLogo]
    , [ThumbnailImageFileId])
    VALUES
    (@Status,
     @Url,
     @StepAmount,
     @MinAmount,
     @RebatePercentage,
     @InvoicePercentage,
     @VATPercentage,
     @EVoucherA,
     @DeliveryCost,
     @IncludeDelivery,
     @VoucherAgreementName,
     @SupplierId,
     @ThumbnailImage,
     @CreationDate,
     @DeletionDate,
     @LowestPrice,
     @InvoicePriceVA,
     @Email,
     @EmailType,
     @MainInsuranceCompanyId,
     @IsSupplierUrl,
     @IsSupplierLogo,
     @ThumbnailImageFileId)

	DECLARE @VoucherAgreementId INT = SCOPE_IDENTITY()

	INSERT INTO Text_Voucher (LCID, Description, Conditions, Limitations, VoucherAgreementId)
		VALUES (@cultureId, @VoucherAgreementName, @Conditions, @Limitations, @VoucherAgreementId)

	IF @isScalepoint = 1
		INSERT INTO [PseudocatVouchers] (PseudoCategoryId, VoucherAgreementId, insuranceCompanyId)
		SELECT c.id, @VoucherAgreementId, p.ICRFNBR FROM @categories c
			CROSS APPLY (SELECT ICRFNBR FROM dbo.INSCOMP WHERE ICPRFNBR IS NULL) AS p
	ELSE
		INSERT INTO [PseudocatVouchers] ([PseudoCategoryId]
      ,[VoucherAgreementId]
      ,[insuranceCompanyId]) SELECT c.id, @VoucherAgreementId, @MainInsuranceCompanyId FROM @categories c

	INSERT INTO PSEUDOCAT_ModelRel
		SELECT @PseudocatModelId, c.id FROM @categories c
			WHERE c.id NOT IN (SELECT PseudoCatId FROM PSEUDOCAT_ModelRel WHERE PseudoCatModelId = @PseudocatModelId)

	PRINT 'Voucher Agreement was successfully created with id = ' + CAST(@VoucherAgreementId AS VARCHAR(32))

COMMIT TRANSACTION

SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_voucher_agreement]