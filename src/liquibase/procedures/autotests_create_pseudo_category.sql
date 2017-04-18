--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_pseudo_category]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_create_pseudo_category]
GO

CREATE PROCEDURE [dbo].[autotests_create_pseudo_category]
		@PseudoCategoryText VARCHAR(510),
		@PseudoCategoryGroupName VARCHAR(254),
		@ModelName VARCHAR(255)
AS

	SET NOCOUNT ON

	IF EXISTS (SELECT pseudocategoryid FROM PsuedoCategory pc
		JOIN Text_Pseudocat tp on pc.PseudoCategoryText = tp.textid
		JOIN PSEUDOCAT_Group pg on pc.PseudoCatGroupId =  pg.pseudoCatGroupId
		JOIN Text_PSEUDOCAT_Group tpg on pg.groupTextId = tpg.textid
		WHERE name = @PseudoCategoryText and groupname = @PseudoCategoryGroupName)
		RETURN
	ELSE
		BEGIN
			INSERT INTO [dbo].[Text]
			VALUES (13)
			DECLARE @idPseudoCategoryGroupText INT = SCOPE_IDENTITY();
			INSERT INTO [dbo].[Text_PSEUDOCAT_Group]
			VALUES(@idPseudoCategoryGroupText,1030,@PseudoCategoryGroupName)
			INSERT INTO [dbo].[PSEUDOCAT_Group]
			VALUES(@idPseudoCategoryGroupText,1,1)
			DECLARE @idPseudoCategoryGroup INT = SCOPE_IDENTITY();

			INSERT INTO [dbo].[Text]
			VALUES (6)
			DECLARE @idPseudoCategoryText INT = SCOPE_IDENTITY();
			INSERT INTO [dbo].[Text_Pseudocat]
			VALUES(@idPseudoCategoryText,1030,@PseudoCategoryText)

			DECLARE @UNIQUEX UNIQUEIDENTIFIER
			SET @UNIQUEX = NEWID();

			INSERT INTO [dbo].[PsuedoCategory]
			(Published,Category,Token,WebSearchCategory,PseudoCategoryText,PseudoCatGroupId)
			VALUES(1,NULL,@UNIQUEX,NULL,@idPseudoCategoryText,@idPseudoCategoryGroup);
			DECLARE @idPseudoCategory INT = SCOPE_IDENTITY();

			update PSEUDOCAT_Group
			set defaultpseudocatid = @idPseudoCategory
			where pseudocatgroupid = @idPseudoCategoryGroup

			DECLARE @PseudoCatModelId INT = (SELECT PseudoCatModelId FROM PSEUDOCAT_Model WHERE ModelName = @ModelName)

			INSERT INTO [dbo].[PSEUDOCAT_ModelRel]
			VALUES(@PseudoCatModelId, @idPseudoCategory)
		END
	SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_pseudo_category]