--liquibase formatted sql
--changeset igu:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_create_choice_reasons]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
  DROP PROCEDURE [dbo].[autotests_create_choice_reasons]
GO

CREATE PROCEDURE [dbo].[autotests_create_choice_reasons]
    @InsCompanyId INT
AS

SET NOCOUNT ON

BEGIN TRANSACTION

CREATE TABLE #tmp
(
  id INT IDENTITY(1, 1) primary key,
  reasonName NVARCHAR(500)
);

insert into #tmp (reasonName) values
('Sagen er generelt veldokumenteret, derfor OK'),
('Sagen er generelt dårligt dokumenteret, derfor lavere skøn'),
('Afviger fra den generelle afskrivning jf. betingelserne (f.eks. FUF)'),
('Vurderes til bedste løsning, tæt på bedste valg'),
('Andet (begrundelse skrives i Rumba)')

declare @id int
declare @reason nvarchar(500)

WHILE EXISTS (SELECT * FROM #tmp)
BEGIN
  SELECT TOP 1 @id = id, @reason = reasonName FROM #tmp ORDER BY id ASC
  IF NOT EXISTS (SELECT * FROM ChoiceReason WHERE ReasonName = @reason AND ReasonType = 1 AND InsuranceCompanyId = @InsCompanyId)
  BEGIN
    INSERT INTO ChoiceReason VALUES (@reason, 1, @InsCompanyId, 1, NULL)
  END

  DELETE #tmp WHERE id = @id
END

DROP TABLE #tmp

COMMIT TRANSACTION

SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_create_choice_reasons]