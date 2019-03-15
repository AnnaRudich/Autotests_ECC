--liquibase formatted sql
--changeset ipo:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_add_popularity]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
  DROP PROCEDURE [dbo].[autotests_add_popularity]
GO

CREATE PROCEDURE [dbo].[autotests_add_popularity]
	@categoryNumber NVARCHAR(256),
	@descriptionPart NVARCHAR(256)
AS

SET NOCOUNT ON

INSERT INTO ProductCustomPopularity (ProductId, Rating)
SELECT v.ProductID, 10
FROM Product v
JOIN Product p on v.ParentProduct = p.ProductID
JOIN CGPRREL cg on cg.CPPRNBR = p.ProductID
JOIN CATEGORY c on cg.CPCGNBR = c.CGRFNBR
WHERE v.Published = 1
AND p.Description LIKE concat('%', @descriptionPart, '%')
AND c.CGNBR =  @categoryNumber

SET NOCOUNT OFF

--rollback DROP FUNCTION [dbo].[autotests_add_popularity]