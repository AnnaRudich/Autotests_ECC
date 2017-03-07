--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_user]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
  DROP PROCEDURE [dbo].[autotests_create_user]
GO

CREATE PROCEDURE [dbo].[autotests_create_user]
	@insCompanyId BIGINT,
  @username NVARCHAR(256),
	@userrole INT = 10,
	@email NVARCHAR(256) = 'ecc_auto@scalepoint.com',
	@password VARCHAR(256) = '12341234'
AS

SET NOCOUNT ON

DECLARE @passwordType CHAR(1) = 'C'
DECLARE @USERTYPE_ADMIN BIGINT = 4
DECLARE @USERROLE_ITMANAGER BIGINT = 1
DECLARE @firstname NVARCHAR(256) = 'FirstName'
DECLARE @lastname NVARCHAR(256) = 'LastName'
DECLARE @enableDND bit = 0
DECLARE @departmentToken NVARCHAR(50) = (SELECT TOP 1 d.DepartmentToken FROM dbo.INSCOMP ic
	LEFT JOIN dbo.Department pd ON pd.DepartmentToken = ic.icDepartment
	LEFT JOIN dbo.Department d ON d.Parent = pd.DepartmentToken WHERE ic.ICRFNBR = @insCompanyId)
DECLARE @cultureId INT = (SELECT LCID FROM Culture c JOIN FormattingProperties f ON c.Culture = f.VALUE WHERE f.[KEY] = 'scalepoint_culture_code')
DECLARE @userId INT = (SELECT UserId FROM [User] WHERE UserName = @username)
DECLARE @shopperAddressId INT = (SELECT max(ShopperAddressId) + 1 FROM [ShopperAddress])
DECLARE @hashedPassword NVARCHAR(256) = 'wSmzJK7mYrBOzPaLq7qFhRNG3/k='
DECLARE @hashType VARCHAR(10) = 'SHA1'

BEGIN TRANSACTION

  IF @userId IS NOT NULL
  BEGIN
    IF NOT EXISTS(SELECT 1 FROM [UserRoleMapping] WHERE UserId = @userId)
      INSERT INTO UserRoleMapping (UserId, RoleId) VALUES (@userId, @USERROLE_ITMANAGER)
    RETURN
  END

	SET @userId = (SELECT max(UserId) + 1 FROM [User])

  SET IDENTITY_INSERT [User] ON
    INSERT INTO [User] (UserId, InsuranceCompany, UserType, UserName, Email, CreditLimit, OldCreditLimit,
                        TotalProductPrice, Deductable, TradeUpMax, CreateClaimAccess, ClaimNumberAccessUpdate, EncryptedPassword, PasswordType,
                        DepartmentToken, CultureId, PasswordChangeStatus, DateCreated, CreatedBy, enableDnDComparison, EncryptedPasswordHashType)
                VALUES (@userId, @insCompanyId, @userrole, @username, @email, 0.00, 0.00,
                        0.00, 0.00, 99999.00, 1, 1, @hashedPassword, @passwordType,
                        @departmentToken, @cultureId, 0, GETDATE(), NULL, @enableDND, @hashType)
	SET IDENTITY_INSERT [User] OFF

  SET IDENTITY_INSERT [ShopperAddress] ON
    INSERT INTO ShopperAddress (ShopperAddressId, Shopper, NickName, FirstName, MiddleName, LastName, AddressLine1, AddressLine2, ZipCode, City, AddressFlag, Cellphone)
                        VALUES (@shopperAddressId, @userId, @username, @firstname, '', @lastname, 'Any Street 15', 'STRATHMORE AVENUE', 'SE25 4TE', 'LONDON', 'P', '07561 285142')
	SET IDENTITY_INSERT [ShopperAddress] OFF

  IF (@userrole&@USERTYPE_ADMIN != 0)
	  INSERT INTO UserRoleMapping (UserId, RoleId) VALUES (@userId, @USERROLE_ITMANAGER)

	PRINT 'User was successfully created with id = ' + CAST(@userId AS VARCHAR)

COMMIT TRANSACTION

SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_user]