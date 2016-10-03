--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT 1 FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_splitstr_on_ints]') AND [type] IN ('FN', 'FT', 'TF', 'IF'))
  DROP FUNCTION [dbo].[autotests_splitstr_on_ints]
GO

CREATE function dbo.autotests_splitstr_on_ints
(
    @String VARCHAR(MAX)
)
returns @SplittedValues TABLE
(
    id bigint PRIMARY KEY
)
AS
BEGIN
    DECLARE @SplitLength INT, @Delimiter VARCHAR(5)

    SET @Delimiter = ','

    while len(@String) > 0
    BEGIN
        SELECT @SplitLength = (CASE charindex(@Delimiter,@String) WHEN 0 THEN
            len(@String) ELSE charindex(@Delimiter,@String) -1 END)

        INSERT INTO @SplittedValues
        SELECT CAST(SUBSTRING(@String,1,@SplitLength) AS BIGINT)

        SELECT @String = (CASE (len(@String) - @SplitLength) WHEN 0 THEN  ''
            ELSE RIGHT(@String, len(@String) - @SplitLength - 1) END)
    END
return
END
--rollback DROP FUNCTION [dbo].[autotests_splitstr_on_ints]