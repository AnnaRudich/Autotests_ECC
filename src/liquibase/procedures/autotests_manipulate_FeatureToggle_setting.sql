--liquibase formatted sql
--changeset aru:1 runOnChange:true endDelimiter:GO

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[autotests_manipulate_FeatureToggle_setting]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
	DROP PROCEDURE [dbo].[autotests_manipulate_FeatureToggle_setting]
GO

CREATE PROCEDURE [dbo].[autotests_manipulate_FeatureToggle_setting]
@FeatureToggleUID varchar(100),
@ToggleState int

AS

update [FF4J_FEATURES]
    set [ENABLE] = @ToggleState
    where FEAT_UID=@FeatureToggleUID



--rollback DROP FUNCTION [dbo].[autotests_manipulate_FeatureToggle_setting]