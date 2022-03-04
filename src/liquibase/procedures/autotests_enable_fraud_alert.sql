--liquibase formatted sql
--changeset aru:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_enable_fraud_alert]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_enable_fraud_alert]
GO


CREATE PROCEDURE [dbo].[autotests_enable_fraud_alert]
@name nvarchar(254),
@eventsConfiguration NVARCHAR(256) = '{"validationEngineEventsEnabled":false,"selfServiceEventsEnabled":false,"unifiedPaymentEventsEnabled":true,"reopenEventsEnabled":false,"rvEventsEnabled":false,"invoiceEventsEnabled":false,"fraudEventsEnabled":true,"attachmentEventsEnabled":true}'
AS
	SET NOCOUNT ON

	UPDATE [INSCOMP]
	SET [eventsConfiguration] = @eventsConfiguration
    WHERE [CompanyCode] = @NAME

    UPDATE [INSCOMP_Audit]
    SET [eventsConfiguration] = @eventsConfiguration
    WHERE [CompanyCode] = @NAME

	SET NOCOUNT OFF