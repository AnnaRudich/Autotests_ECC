--liquibase formatted sql
--changeset aru:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_add_test_mobile_number]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_add_test_mobile_number]
GO


CREATE PROCEDURE [dbo].[autotests_add_test_mobile_number]
    @testMobileNumber nvarchar(50),
	@testMobileOwner nvarchar(100)

AS
	SET NOCOUNT ON

	INSERT INTO [dbo].[TestMobileNumber] ([TestMobileNumber], [TestMobileOwner])
	VALUES (@testMobileNumber, @testMobileOwner);

	SET NOCOUNT OFF