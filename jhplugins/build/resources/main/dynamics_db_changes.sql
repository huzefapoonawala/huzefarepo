/****** Object:  Table [dbo].[WebCategory]    Script Date: 04/01/2014 15:21:48 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[WebCategory](
	[DEPT_CODE] [nvarchar](10) NOT NULL,
	[SUB_CODE] [nvarchar](10) NOT NULL,
	[CATEGORY_CODE] [nvarchar](10) NOT NULL,
	[NAME] [nvarchar](30) NOT NULL,
	[CREATION_DATE] [datetime] NOT NULL
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[WebCategory] ADD  CONSTRAINT [DF_WebCategory_CREATION_DATE]  DEFAULT (getdate()) FOR [CREATION_DATE]
GO



/****** Object:  Table [dbo].[WebItem]    Script Date: 04/01/2014 15:22:17 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[WebItem](
	[SKU] [nvarchar](7) NOT NULL,
	[FACTORY] [nvarchar](17) NULL,
	[NOMENCLATURE_EXT] [nvarchar](30) NULL,
	[WIDTH] [float] NULL,
	[HEIGHT] [float] NULL,
	[LENGTH] [float] NULL,
	[WEIGHT] [float] NULL,
	[VENDOR_NUMBER] [nvarchar](7) NULL,
	[CATALOG_VENDOR_NAME] [nvarchar](35) NULL,
	[NOMENCLATURE] [nvarchar](30) NULL,
	[CATALOG_PAGE_NUMBER] [nvarchar](8) NULL,
	[SUGGESTED_RETAIL] [float] NULL,
	[SELLING_UNIT] [nvarchar](5) NULL,
	[BUYER] [nvarchar](20) NULL,
	[IMAGE] [nvarchar](25) NULL,
	[PRO_BENCHMARK_RETAIL] [float] NULL,
	[UPC_CODE] [nvarchar](12) NULL,
	[REG_PRICE] [float] NULL,
	[VP1_PRICE] [float] NULL,
	[VP2_PRICE] [float] NULL,
	[ADV_PRICE] [float] NULL,
	[RETAIL_SENSITIVITY] [nvarchar](3) NULL,
	[BENCHMARK_RETAIL] [float] NULL,
	[QUANTITY_ROUND_OPTION] [nvarchar](1) NULL,
	[OLD_UPC] [nvarchar](12) NULL,
	[PRO_SENSITIVITY_CODE] [int] NULL,
	[VENDOR_UNIT_OF_MEASURE] [nvarchar](6) NULL,
	[CLAIMS_CODE] [nvarchar](1) NULL,
	[CLAIMS_MEMO_FLAG] [tinyint] NULL,
	[REPLACEMENT_ITEM] [int] NULL,
	[SUBSTITUTE_ITEM] [int] NULL,
	[COUNTRY_CODE] [nvarchar](2) NULL,
	[HARMONIZED_CODE_1] [int] NULL,
	[HARMONIZED_CODE_2] [int] NULL,
	[HARMONIZED_CODE_3] [int] NULL,
	[BUYING_DEPT_DESC] [nvarchar](30) NULL,
	[HAZARDOUS_INLAND] [nvarchar](1) NULL,
	[HAZARDOUS_MARINE] [nvarchar](1) NULL,
	[KIT_ITEM_SWITCH] [nvarchar](1) NULL,
	[KIT_ITEM_KEY] [nvarchar](20) NULL,
	[CONTAINER_UPC] [nvarchar](14) NULL,
	[RETAIL_UNIT_OF_MEASURE] [nvarchar](6) NULL,
	[CUBIC_DIVISOR] [int] NULL,
	[SHIP_BY_UPS_FEDEX_USPS] [nvarchar](1) NULL,
	[DEPT_ID] [nvarchar](15) NULL,
	[CATEGORY_ID] [nvarchar](15) NULL,
	[IS_UPDATED] [tinyint] NULL,
	[QUANTITY_IN_STOCK] [int] NULL,
	[IS_ON_WEBSITE] [tinyint] NULL,
 CONSTRAINT [PK_WebItem] PRIMARY KEY CLUSTERED 
(
	[SKU] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[WebItem] ADD  CONSTRAINT [DF_WebItem_IS_UPDATED]  DEFAULT ((0)) FOR [IS_UPDATED]
GO

ALTER TABLE [dbo].[WebItem] ADD  CONSTRAINT [DF_WebItem_QUANTITY_IN_STOCK]  DEFAULT ((0)) FOR [QUANTITY_IN_STOCK]
GO

ALTER TABLE [dbo].[WebItem] ADD  CONSTRAINT [DF_WebItem_IS_ON_WEBSITE]  DEFAULT ((0)) FOR [IS_ON_WEBSITE]
GO



/****** Object:  StoredProcedure [dbo].[GetDetailsForPO]    Script Date: 04/01/2014 15:23:01 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================

CREATE PROCEDURE [dbo].[GetDetailsForPO]
	-- Add the parameters for the stored procedure here
	@supplierId int, @fromDate1 datetime, @toDate1 datetime, @fromDate2 datetime, @toDate2 datetime
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
select * from (
	select 
		i.restocklevel,
		i.reorderpoint as reorderLevel,
		i.lastsold, 
		i.LastReceived,
		i.inactive, 
		i.cost as costPrice, 
		i.price as retailPrice,
		i.id,
		i.ItemLookupCode as sku,
		i.Description,
		i.quantity as stockQuantity,
		isnull(t1.onOrder,0) as onOrder,
		isnull(t1.transferOut,0) as transferOut,
		i.QuantityCommitted as committedQuantity,
		i.supplierid, 
		--sum(t2.quantity) as QuantitySold,
		--sum(CASE WHEN t2.Time BETWEEN @fromDate2 AND @toDate2 THEN t2.Quantity ELSE 0 end) as quantitySoldRecently,
		t2.*,
		s.SupplierName,
		(i.restocklevel-((i.Quantity+isnull(t1.onOrder,0))-(i.QuantityCommitted+isnull(t1.transferOut,0)))) as orderQuantity,
		left(a.aliases, len(a.aliases)-1) as aliases,
		BinLocation
	from 
		Item i 
		join Supplier s
			on i.SupplierID = s.id
		OUTER APPLY(
			SELECT sum(te.Quantity) as quantitySold,
					sum(CASE WHEN t.Time BETWEEN @fromDate2 AND @toDate2 THEN te.Quantity ELSE 0 end) as quantitySoldRecently
			FROM transactionentry te
				inner join [Transaction] t on t.TransactionNumber = te.TransactionNumber
			WHERE te.ItemID = i.ID
				AND (t.Time between @fromDate1 and @toDate1 OR t.Time between @fromDate2 AND @toDate2)
				and te.quantity>0 
		) t2
		outer APPLY (
			SELECT ItemID, 
				sum(case when POType = 0 then QuantityOrdered-QuantityReceivedToDate ELSE 0 end) as onOrder,
				sum(case when POType = 3 then QuantityOrdered-QuantityReceivedToDate ELSE 0 end) as transferOut
			FROM PurchaseOrderEntry p1 
				INNER JOIN PurchaseOrder p2 ON p2.ID = p1.PurchaseOrderID 
			WHERE p2.POType IN (0,3) AND p2.Status IN (0,1) AND p1.ItemID = i.ID
			GROUP BY p1.ItemID
		) t1
		OUTER APPLY (
			SELECT aliases = (SELECT Alias+',' FROM Alias a where a.ItemID = i.ID FOR XML PATH(''))
		) a
	where 
		((
			t2.quantitySold > 0 and
			((i.Quantity+isnull(t1.onOrder,0)) - (i.QuantityCommitted+isnull(t1.transferOut,0))) <= i.ReOrderpoint
			and i.RestockLevel <> 0
		)
		or
		((i.QuantityCommitted+isnull(t1.transferOut,0)) - (i.Quantity+isnull(t1.onOrder,0))) > 0)
		and i.SupplierId=@supplierId 
		and i.ItemType <> 7
		--and ((i.RestockLevel + i.QuantityCommitted) - i.ReorderPoint) > 0 
	--group by i.itemlookupcode,i.Description,s.suppliername,i.id, i.inactive, i.lastsold, i.LastReceived, i.restocklevel, i.reorderpoint, i.quantity,i.QuantityCommitted,i.supplierid,i.price,i.cost, t1.onOrder, t1.transferOut, a.aliases
) t1
order by SupplierName,sku

END

GO



/****** Object:  StoredProcedure [dbo].[GetItemDetails]    Script Date: 04/01/2014 15:23:38 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[GetItemDetails] 
	-- Add the parameters for the stored procedure here
	@xml xml
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;
	SET ARITHABORT ON;
    -- Insert statements for procedure here
select 
	i.restocklevel,
	i.reorderpoint as reorderLevel,
	i.lastsold, 
	i.inactive, 
	i.cost as costPrice, 
	i.price as retailPrice,
	i.id,
	i.ItemLookupCode as sku,
	i.Description,
	i.quantity as stockQuantity,
	i.QuantityCommitted as committedQuantity, 
	isnull(t1.onOrder,0) as onOrder,
	isnull(t1.transferOut,0) as transferOut,
	i.LastReceived,
	left(a.aliases, len(a.aliases)-1) as aliases,
	i.BinLocation
from 
	Item i 
	OUTER APPLY (
		SELECT aliases = (SELECT Alias+',' FROM Alias a where a.ItemID = i.ID FOR XML PATH(''))
	) a
	outer APPLY (
		SELECT ItemID, 
			sum(case when POType = 0 then QuantityOrdered-QuantityReceivedToDate ELSE 0 end) as onOrder,
			sum(case when POType = 3 then QuantityOrdered-QuantityReceivedToDate ELSE 0 end) as transferOut
		FROM PurchaseOrderEntry p1 
			INNER JOIN PurchaseOrder p2 ON p2.ID = p1.PurchaseOrderID 
		WHERE p2.POType IN (0,3) AND p2.Status IN (0,1) AND p1.ItemID = i.ID
		GROUP BY p1.ItemID
	) t1
where 
	i.ItemLookupCode IS NOT NULL
	AND
	(
		@xml.exist('/request/sku[.=sql:column("i.ItemLookupCode")]') = 1
		--i.ItemLookupCode = @xml.value('(/request/sku)[1]','nvarchar(75)')
		OR 
		i.ID IN (SELECT ItemID from Alias WHERE Alias = @xml.value('(/request/sku)[1]','nvarchar(75)') )
	)
END

GO



/****** Object:  UserDefinedFunction [dbo].[GetWebCategories]    Script Date: 04/10/2014 16:37:56 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE FUNCTION [dbo].[GetWebCategories] 
(
	-- Add the parameters for the function here
	@request xml
)
RETURNS 
@pct TABLE 
(
	-- Add the column definitions for the TABLE variable here
	CATEGORY_ID nvarchar(20), PARENT_WEBSITE_CATEGORY_IDS nvarchar(MAX)
)
AS
BEGIN
	DECLARE @ct table(categoryId nvarchar(20));
	INSERT INTO @ct
	SELECT d.value('@id','nvarchar(20)') from @request.nodes('/request/category') as ref(d);
	
	;WITH cte as (
		select CATEGORY_ID as cateId,* FROM web_categories WITH(NOLOCK) where CATEGORY_ID IN (SELECT categoryId from @ct)
		UNION ALL
		SELECT c.cateId,p.* FROM cte c inner JOIN web_categories p ON c.PARENT_ID = p.CATEGORY_ID
	)
	INSERT INTO @pct
	SELECT CATEGORY_ID, c.* from web_categories w WITH (NOLOCK)
	CROSS APPLY(
		SELECT parentIds = stuff((select ','+cast(WEBSITE_CATEGORY_ID AS NVARCHAR(10)) FROM cte where cateId = w.CATEGORY_ID and HIERARCHY_LEVEL <= 3 AND (WEBSITE_CATEGORY_ID IS NOT NULL AND WEBSITE_CATEGORY_ID > 0) FOR XML PATH('')),1,1,'')
	) c
	WHERE EXISTS (SELECT categoryId from @ct where w.CATEGORY_ID = categoryId)
	
	RETURN 
END

GO

