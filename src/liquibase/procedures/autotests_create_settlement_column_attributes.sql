--liquibase formatted sql
--changeset aru:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_settlement_column_attributes]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_create_settlement_column_attributes]
GO


CREATE PROCEDURE [dbo].[autotests_create_settlement_column_attributes]
@modelId INT,
AS
	SET NOCOUNT ON

	INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'categoryGroupColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'quantityColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'settlementAgeColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'totalPurchasePriceColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'depreciationColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'discretionaryDepreciationColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'replacementAmountColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'cashPayoutColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'policyDepreciationColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'newItemColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'roomColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'depreciationTypeColumn', 0, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'valuationCustomerDemandColumn', 0, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'valuationNewPriceColumn', 0, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'valuationPurchasePriceColumn', 0, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'valuationDiscretionaryPriceColumn', 0, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'valuationUsedPriceColumn', 0, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'valuationRepairPriceColumn', 0, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'valuationRepairEstimateColumn', 0, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'voucherAgreementNameColumn', 0, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'productMatchSupplierNameColumn', 0, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'activeValuationTypeColumn', 0, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'acquiredColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'typeColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'voucherImageColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'hasAttachmentColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'claimLineIDColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'descriptionColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'companySpecificColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'repairValuationColumn', 1, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'originalDescriptionColumn', 0, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'voucherPurchaseAmountValueColumn', 0, NULL, NULL)
    INSERT INTO [dbo].[SettlementColumnAttributes] (modelId, columnId, visible, position, width) VALUES(@modelId, 'auditRequestInfoColumn', 0, NULL, NULL)


	SET NOCOUNT OFF