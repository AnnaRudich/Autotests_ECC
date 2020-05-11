--liquibase formatted sql
--changeset aru:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_add_damageTypes]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_add_damageTypes]
GO


CREATE PROCEDURE [dbo].[autotests_add_damageTypes]

AS
	SET NOCOUNT ON

	UPDATE [PsuedoCategory]
	SET damageTypes = 'damageType1,damageType2'
    WHERE PseudoCategoryID = 209083 --Category Personal Medicine
    
    UPDATE [dbo].[PsuedoCategory]
    SET [damageTypes] = 'Væskeskade,Kortslutning,Tabt/itu/flækket,Brandskade' 
    where [PseudoCategoryText] = (select [TextId] from [QA10_ECC_DK].[dbo].[Text_Pseudocat] where [Name] = 'Videokamera')

	SET NOCOUNT OFF