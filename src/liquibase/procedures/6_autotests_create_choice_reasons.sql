--liquibase formatted sql
--changeset igu:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_choice_reasons]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
  DROP PROCEDURE [dbo].[autotests_create_choice_reasons]
GO

CREATE PROCEDURE [dbo].[autotests_create_choice_reasons]
    @InsCompanyId INT
AS

SET NOCOUNT ON

INSERT INTO ChoiceReason VALUES
            ('Sagen er generelt veldokumenteret, derfor OK', 1, @InsCompanyId, 1, NULL),
            ('Sagen er generelt dårligt dokumenteret, derfor lavere skøn', 1, @InsCompanyId, 1, NULL),
            ('Afviger fra den generelle afskrivning jf. betingelserne (f.eks. FUF)', 1, @InsCompanyId, 1, NULL),
            ('Vurderes til bedste løsning, tæt på bedste valg', 1, @InsCompanyId, 1, NULL),
            ('Andet (begrundelse skrives i Rumba)', 1, @InsCompanyId, 1, NULL);

PRINT 'Choice reasons created for company with id = ' + CAST(@InsCompanyId AS VARCHAR(32))

COMMIT TRANSACTION

SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_choice_reasons]