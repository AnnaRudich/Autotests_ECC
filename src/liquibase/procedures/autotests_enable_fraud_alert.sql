--liquibase formatted sql
--changeset aru:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_enable_fraud_alert]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_enable_fraud_alert]
GO


CREATE PROCEDURE [dbo].[autotests_enable_fraud_alert]
@NAME nvarchar(254)

AS
	SET NOCOUNT ON

	UPDATE [INSCOMP]
	SET [useFraudCheck] = 1
    WHERE [ICNAME] = @NAME

    UPDATE [INSCOMP_Audit]
    SET [useFraudCheck] = 1
    WHERE [ICNAME] = @NAME

	SET NOCOUNT OFF