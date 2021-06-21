--liquibase formatted sql
--changeset aru:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_add_ip2_test_entries]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_add_ip2_test_entries]
GO

CREATE PROCEDURE [dbo].[autotests_add_ip2_test_entries]
@companyId int,
@eventId int,
@baseDocumentId int,
@mockEndpoint nvarchar(256),
@supplierId int

AS

SET NOCOUNT ON

IF NOT EXISTS (Select TransportModuleGUID from [dbo].[ExternalIntegrationConfiguration_TransportModule] where Code ='GENERIC_DK_WS')
INSERT INTO [dbo].[ExternalIntegrationConfiguration_TransportModule]
(Code, [Description])
VALUES ('GENERIC_DK_WS', 'Test transport for autotests test');

/* adding events configuration for test IC*/


--SET IDENTITY_INSERT [dbo].[ExternalIntegrationConfiguration_Entry]  OFF


DECLARE @testTransportModuleGuid int = (Select TransportModuleGUID from [dbo].[ExternalIntegrationConfiguration_TransportModule] where Code ='GENERIC_DK_WS')

INSERT INTO [dbo].[ExternalIntegrationConfiguration_Entry] (
       [CompanyId]
      ,[EventId]
      ,[BaseDocumentId]
      ,[Transformation]
      ,[TransportId]
      ,[ProductionEndpoint]
      ,[TestEndpoint]
      ,[SuccessCode]
      ,[EnvelopeId]
      ,[AuthToken]
      ,[SchemaFile]
      ,[WebServiceTargetNameSpace]
      ,[OperationTargetNameSpace]
      ,[TestWebServiceTargetNameSpace]
      ,[TestOperationTargetNameSpace]
      ,[UseSavedNameSpaces]
      ,[CryptKey]
      ,[TestCryptKey]
      ,[WSDLBlocked]
      ,[SupplierId]
      ,[TestUserName]
      ,[UserName]
      ,[Salt]
      ,[TestSalt]
)
VALUES (
@companyId,
@eventId,
@baseDocumentId,
NULL,
@testTransportModuleGuid,
@mockEndpoint,
@mockEndpoint,
'200',
NULL,
NULL,
NULL,
NULL,
NULL,
NULL,
NULL,
0,
NULL,
NULL,
0,
@supplierId,
NULL,
NULL,
NULL,
NULL)

SET NOCOUNT OFF
