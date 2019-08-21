--liquibase formatted sql
--changeset aru:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_performance_test_users]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_create_performance_test_users]
GO


CREATE PROCEDURE [dbo].[autotests_create_performance_test_users]
    @name NVARCHAR(256),
    @id int,
    @userRights int = 10,
    @amount int

AS
	SET NOCOUNT ON

	DECLARE @a int = 0
	DECLARE @username NVARCHAR(256)
	WHILE @a < 100
	BEGIN
	    SET @a = @a + 1
	    SET @username = concat(@name, @a)
	    EXEC autotests_create_user @id, @username, @userRights
	END

	SET NOCOUNT OFF