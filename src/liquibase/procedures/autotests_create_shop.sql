--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_shop]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
  DROP PROCEDURE [dbo].[autotests_create_shop]
GO

CREATE PROCEDURE [dbo].[autotests_create_shop]
    @ShopName nvarchar(50),
	  @SupplierID int,
	  @PostalCode NVARCHAR(20),
	  @IsRetailShop bit,
	  @IsRepairValuationLocation bit,
	  @PickupId int OUTPUT
AS

SET NOCOUNT ON

  declare @Address1 varchar(100) = 'Test address 1'
  declare @Address2 varchar(100) = 'Test address 2'
  declare @Phone varchar(100) = '0800 0833113'
  declare @City varchar(100) = 'Test city'

  insert into Shop (SupplierId,name,zipCode,address1,address2,city,phone,searchZipCode,typeRetail,typeRepairValuation,typeOnline, updatedDate)
  select @SupplierID, @ShopName, @PostalCode, @Address1, @Address2, @City, @Phone, @PostalCode, @IsRetailShop, @IsRepairValuationLocation, 0, GETDATE()

  select @PickupId = @@IDENTITY

	RETURN

SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_shop]