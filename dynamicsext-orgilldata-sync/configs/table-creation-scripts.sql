USE [orgillsync]
GO

/****** Object:  Table [dbo].[WebCategory]    Script Date: 10/8/2017 8:40:03 AM ******/
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

ALTER TABLE [dbo].[WebCategory] ADD  CONSTRAINT [DF_OS_WebCategory_CREATION_DATE]  DEFAULT (getdate()) FOR [CREATION_DATE]
GO

/****** Object:  Table [dbo].[WebItem]    Script Date: 10/8/2017 8:40:28 AM ******/
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
 CONSTRAINT [PK_WebItem] PRIMARY KEY CLUSTERED 
(
	[SKU] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[WebItem] ADD  CONSTRAINT [DF_OS_WebItem_IS_UPDATED]  DEFAULT ((0)) FOR [IS_UPDATED]
GO


/****** Object:  Table [dbo].[Department]    Script Date: 10/8/2017 8:42:51 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Department](
	[HQID] [int] NOT NULL,
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](30) NOT NULL,
	[code] [nvarchar](17) NOT NULL,
	[DBTimeStamp] [timestamp] NULL,
	[PID] [int] NULL,
 CONSTRAINT [PK_Department] PRIMARY KEY NONCLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[Department] ADD  CONSTRAINT [DF_OS_Department_HQID]  DEFAULT (0) FOR [HQID]
GO

ALTER TABLE [dbo].[Department] ADD  CONSTRAINT [DF_OS_Department_Name]  DEFAULT ('') FOR [Name]
GO

ALTER TABLE [dbo].[Department] ADD  CONSTRAINT [DF_OS_Department_Number]  DEFAULT ('') FOR [code]
GO

/****** Object:  Table [dbo].[Category]    Script Date: 10/8/2017 8:43:15 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Category](
	[HQID] [int] NOT NULL,
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[DepartmentID] [int] NOT NULL,
	[Name] [nvarchar](30) NOT NULL,
	[Code] [nvarchar](17) NOT NULL,
	[DBTimeStamp] [timestamp] NULL,
	[PID] [int] NULL,
 CONSTRAINT [PK_Category] PRIMARY KEY NONCLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[Category] ADD  CONSTRAINT [DF_OS_Category_HQID]  DEFAULT (0) FOR [HQID]
GO

ALTER TABLE [dbo].[Category] ADD  CONSTRAINT [DF_OS_Category_DepartmentID]  DEFAULT (0) FOR [DepartmentID]
GO

ALTER TABLE [dbo].[Category] ADD  CONSTRAINT [DF_OS_Category_Name]  DEFAULT ('') FOR [Name]
GO

ALTER TABLE [dbo].[Category] ADD  CONSTRAINT [DF_OS_Category_Code]  DEFAULT ('') FOR [Code]
GO

/****** Object:  Table [dbo].[Item]    Script Date: 10/8/2017 8:44:05 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Item](
	[BinLocation] [nvarchar](20) NOT NULL,
	[BuydownPrice] [money] NOT NULL,
	[BuydownQuantity] [float] NOT NULL,
	[CommissionAmount] [money] NOT NULL,
	[CommissionMaximum] [money] NOT NULL,
	[CommissionMode] [int] NOT NULL,
	[CommissionPercentProfit] [real] NOT NULL,
	[CommissionPercentSale] [real] NOT NULL,
	[Description] [nvarchar](30) NOT NULL,
	[FoodStampable] [bit] NOT NULL,
	[HQID] [int] NOT NULL,
	[ItemNotDiscountable] [bit] NOT NULL,
	[LastReceived] [datetime] NULL,
	[LastUpdated] [datetime] NOT NULL,
	[QuantityCommitted] [float] NOT NULL,
	[SerialNumberCount] [int] NOT NULL,
	[TareWeightPercent] [float] NOT NULL,
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ItemLookupCode] [nvarchar](25) NOT NULL,
	[DepartmentID] [int] NOT NULL,
	[CategoryID] [int] NOT NULL,
	[MessageID] [int] NOT NULL,
	[Price] [money] NOT NULL,
	[PriceA] [money] NOT NULL,
	[PriceB] [money] NOT NULL,
	[PriceC] [money] NOT NULL,
	[SalePrice] [money] NOT NULL,
	[SaleStartDate] [datetime] NULL,
	[SaleEndDate] [datetime] NULL,
	[QuantityDiscountID] [int] NOT NULL,
	[TaxID] [int] NOT NULL,
	[ItemType] [smallint] NOT NULL,
	[Cost] [money] NOT NULL,
	[Quantity] [float] NOT NULL,
	[ReorderPoint] [float] NOT NULL,
	[RestockLevel] [float] NOT NULL,
	[TareWeight] [float] NOT NULL,
	[SupplierID] [int] NOT NULL,
	[TagAlongItem] [int] NOT NULL,
	[TagAlongQuantity] [float] NOT NULL,
	[ParentItem] [int] NOT NULL,
	[ParentQuantity] [float] NOT NULL,
	[BarcodeFormat] [smallint] NOT NULL,
	[PriceLowerBound] [money] NOT NULL,
	[PriceUpperBound] [money] NOT NULL,
	[PictureName] [nvarchar](50) NOT NULL,
	[LastSold] [datetime] NULL,
	[SubDescription1] [nvarchar](30) NOT NULL,
	[SubDescription2] [nvarchar](30) NOT NULL,
	[SubDescription3] [nvarchar](30) NOT NULL,
	[UnitOfMeasure] [nvarchar](4) NOT NULL,
	[SubCategoryID] [int] NOT NULL,
	[QuantityEntryNotAllowed] [bit] NOT NULL,
	[PriceMustBeEntered] [bit] NOT NULL,
	[BlockSalesReason] [nvarchar](30) NOT NULL,
	[BlockSalesAfterDate] [datetime] NULL,
	[Weight] [float] NOT NULL,
	[Taxable] [bit] NOT NULL,
	[DBTimeStamp] [timestamp] NULL,
	[BlockSalesBeforeDate] [datetime] NULL,
	[LastCost] [money] NOT NULL,
	[ReplacementCost] [money] NOT NULL,
	[WebItem] [bit] NOT NULL,
	[BlockSalesType] [int] NOT NULL,
	[BlockSalesScheduleID] [int] NOT NULL,
	[SaleType] [int] NOT NULL,
	[SaleScheduleID] [int] NOT NULL,
	[Consignment] [bit] NOT NULL,
	[Inactive] [bit] NOT NULL,
	[LastCounted] [datetime] NULL,
	[DoNotOrder] [bit] NOT NULL,
	[MSRP] [money] NOT NULL,
	[DateCreated] [datetime] NOT NULL,
	[UsuallyShip] [nvarchar](255) NOT NULL,
	[NumberFormat] [nvarchar](20) NULL,
	[ItemCannotBeRet] [bit] NULL,
	[ItemCannotBeSold] [bit] NULL,
	[IsAutogenerated] [bit] NULL,
	[IsGlobalvoucher] [bit] NOT NULL,
	[DeleteZeroBalanceEntry] [bit] NULL,
	[TenderID] [int] NOT NULL,
	[Notes] [ntext] NULL,
	[ExtendedDescription] [ntext] NOT NULL,
	[Content] [ntext] NOT NULL,
 CONSTRAINT [PK_Item] PRIMARY KEY NONCLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_BinLocation]  DEFAULT ('') FOR [BinLocation]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_BuydownPrice]  DEFAULT (0) FOR [BuydownPrice]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_BuydownQuantity]  DEFAULT (0) FOR [BuydownQuantity]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_CommissionAmount]  DEFAULT (0) FOR [CommissionAmount]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_CommissionMaximum]  DEFAULT (0) FOR [CommissionMaximum]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_CommissionMode]  DEFAULT (0) FOR [CommissionMode]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_CommissionPercentProfit]  DEFAULT (0) FOR [CommissionPercentProfit]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_CommissionPercentSale]  DEFAULT (0) FOR [CommissionPercentSale]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_Description]  DEFAULT ('') FOR [Description]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_FoodStampable]  DEFAULT (0) FOR [FoodStampable]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_HQID]  DEFAULT (0) FOR [HQID]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_ItemNotDiscountable]  DEFAULT (0) FOR [ItemNotDiscountable]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_LastUpdated]  DEFAULT (getdate()) FOR [LastUpdated]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_QuantityCommitted]  DEFAULT (0) FOR [QuantityCommitted]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_SerialNumberCount]  DEFAULT (0) FOR [SerialNumberCount]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_TareWeightPercent]  DEFAULT (0) FOR [TareWeightPercent]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_ItemLookupCode]  DEFAULT ('') FOR [ItemLookupCode]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_DepartmentID]  DEFAULT (0) FOR [DepartmentID]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_CategoryID]  DEFAULT (0) FOR [CategoryID]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_MessageID]  DEFAULT (0) FOR [MessageID]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_Price]  DEFAULT (0) FOR [Price]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_PriceA]  DEFAULT (0) FOR [PriceA]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_PriceB]  DEFAULT (0) FOR [PriceB]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_PriceC]  DEFAULT (0) FOR [PriceC]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_SalePrice]  DEFAULT (0) FOR [SalePrice]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_QuantityDiscountID]  DEFAULT (0) FOR [QuantityDiscountID]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_TaxID]  DEFAULT (0) FOR [TaxID]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_ItemType]  DEFAULT (0) FOR [ItemType]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_Cost]  DEFAULT (0) FOR [Cost]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_Quantity]  DEFAULT (0) FOR [Quantity]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_ReorderPoint]  DEFAULT (0) FOR [ReorderPoint]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_RestockLevel]  DEFAULT (0) FOR [RestockLevel]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_TareWeight]  DEFAULT (0) FOR [TareWeight]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_SupplierID]  DEFAULT (0) FOR [SupplierID]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_TagAlongItem]  DEFAULT (0) FOR [TagAlongItem]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_TagAlongQuantity]  DEFAULT (0) FOR [TagAlongQuantity]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_ParentItem]  DEFAULT (0) FOR [ParentItem]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_ParentQuantity]  DEFAULT (0) FOR [ParentQuantity]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_BarcodeFormat]  DEFAULT (0) FOR [BarcodeFormat]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_PriceLowerBound]  DEFAULT (0) FOR [PriceLowerBound]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_PriceUpperBound]  DEFAULT (0) FOR [PriceUpperBound]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_PictureName]  DEFAULT ('') FOR [PictureName]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_SubDescription1]  DEFAULT ('') FOR [SubDescription1]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_SubDescription2]  DEFAULT ('') FOR [SubDescription2]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_SubDescription3]  DEFAULT ('') FOR [SubDescription3]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_UnitOfMeasure]  DEFAULT ('') FOR [UnitOfMeasure]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_SubCategoryID]  DEFAULT (0) FOR [SubCategoryID]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_QuantityEntryNotAllowed]  DEFAULT (0) FOR [QuantityEntryNotAllowed]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_PriceMustBeEntered]  DEFAULT (0) FOR [PriceMustBeEntered]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_BlockSalesReason]  DEFAULT ('') FOR [BlockSalesReason]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_Weight]  DEFAULT (0) FOR [Weight]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_Taxable]  DEFAULT (1) FOR [Taxable]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_LastCost]  DEFAULT (0) FOR [LastCost]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_ReplacementCost]  DEFAULT (0) FOR [ReplacementCost]
GO

ALTER TABLE [dbo].[Item] ADD  DEFAULT (0) FOR [WebItem]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_BlockSalesType]  DEFAULT (0) FOR [BlockSalesType]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_BlockSalesScheduleID]  DEFAULT (0) FOR [BlockSalesScheduleID]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_SaleType]  DEFAULT (0) FOR [SaleType]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_SaleScheduleID]  DEFAULT (0) FOR [SaleScheduleID]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_Consignment]  DEFAULT (0) FOR [Consignment]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_Inactive]  DEFAULT (0) FOR [Inactive]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_DoNotOrder]  DEFAULT (0) FOR [DoNotOrder]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_MSRP]  DEFAULT (0) FOR [MSRP]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_DateCreated]  DEFAULT (getdate()) FOR [DateCreated]
GO

ALTER TABLE [dbo].[Item] ADD  CONSTRAINT [DF_OS_Item_UsuallyShip]  DEFAULT ('') FOR [UsuallyShip]
GO

ALTER TABLE [dbo].[Item] ADD  DEFAULT ((0)) FOR [IsGlobalvoucher]
GO

ALTER TABLE [dbo].[Item] ADD  DEFAULT ((0)) FOR [TenderID]
GO

ALTER TABLE [dbo].[Item] ADD  DEFAULT ('') FOR [Notes]
GO

ALTER TABLE [dbo].[Item] ADD  DEFAULT ('') FOR [ExtendedDescription]
GO

ALTER TABLE [dbo].[Item] ADD  DEFAULT ('') FOR [Content]
GO

/****** Object:  Table [dbo].[Alias]    Script Date: 10/8/2017 8:44:22 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Alias](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ItemID] [int] NOT NULL,
	[Alias] [nvarchar](25) NOT NULL,
	[DBTimeStamp] [timestamp] NULL,
 CONSTRAINT [PK_Alias] PRIMARY KEY NONCLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[Alias] ADD  CONSTRAINT [DF_OS_Alias_ItemID]  DEFAULT (0) FOR [ItemID]
GO

ALTER TABLE [dbo].[Alias] ADD  CONSTRAINT [DF_OS_Alias_Alias]  DEFAULT ('') FOR [Alias]
GO

/****** Object:  Table [dbo].[Supplier]    Script Date: 10/10/2017 6:44:11 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Supplier](
	[Country] [nvarchar](20) NOT NULL,
	[HQID] [int] NOT NULL,
	[LastUpdated] [datetime] NOT NULL,
	[State] [nvarchar](20) NOT NULL,
	[ID] [int] NOT NULL,
	[SupplierName] [nvarchar](30) NOT NULL,
	[ContactName] [nvarchar](30) NOT NULL,
	[Address1] [nvarchar](30) NOT NULL,
	[Address2] [nvarchar](30) NOT NULL,
	[City] [nvarchar](30) NOT NULL,
	[Zip] [nvarchar](20) NOT NULL,
	[EmailAddress] [nvarchar](255) NOT NULL,
	[WebPageAddress] [nvarchar](255) NOT NULL,
	[Code] [nvarchar](17) NOT NULL,
	[DBTimeStamp] [timestamp] NULL,
	[AccountNumber] [nvarchar](20) NOT NULL,
	[TaxNumber] [nvarchar](20) NOT NULL,
	[CurrencyID] [int] NOT NULL,
	[PhoneNumber] [nvarchar](30) NOT NULL,
	[FaxNumber] [nvarchar](30) NOT NULL,
	[CustomText1] [nvarchar](30) NOT NULL,
	[CustomText2] [nvarchar](30) NOT NULL,
	[CustomText3] [nvarchar](30) NOT NULL,
	[CustomText4] [nvarchar](30) NOT NULL,
	[CustomText5] [nvarchar](30) NOT NULL,
	[CustomNumber1] [float] NOT NULL,
	[CustomNumber2] [float] NOT NULL,
	[CustomNumber3] [float] NOT NULL,
	[CustomNumber4] [float] NOT NULL,
	[CustomNumber5] [float] NOT NULL,
	[CustomDate1] [datetime] NULL,
	[CustomDate2] [datetime] NULL,
	[CustomDate3] [datetime] NULL,
	[CustomDate4] [datetime] NULL,
	[CustomDate5] [datetime] NULL,
	[Terms] [nvarchar](50) NOT NULL,
	[Notes] [ntext] NOT NULL,
 CONSTRAINT [PK_Supplier] PRIMARY KEY NONCLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_Country]  DEFAULT ('') FOR [Country]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_HQID]  DEFAULT (0) FOR [HQID]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_LastUpdated]  DEFAULT (getdate()) FOR [LastUpdated]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_State]  DEFAULT ('') FOR [State]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_SupplierName]  DEFAULT ('') FOR [SupplierName]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_ContactName]  DEFAULT ('') FOR [ContactName]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_Address1]  DEFAULT ('') FOR [Address1]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_Address2]  DEFAULT ('') FOR [Address2]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_City]  DEFAULT ('') FOR [City]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_Zip]  DEFAULT ('') FOR [Zip]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_EmailAddress]  DEFAULT ('') FOR [EmailAddress]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_WebPageAddress]  DEFAULT ('') FOR [WebPageAddress]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_Code]  DEFAULT ('') FOR [Code]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_AccountNumber]  DEFAULT ('') FOR [AccountNumber]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_TaxNumber]  DEFAULT ('') FOR [TaxNumber]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_CurrencyID]  DEFAULT (0) FOR [CurrencyID]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_PhoneNumber]  DEFAULT ('') FOR [PhoneNumber]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_FaxNumber]  DEFAULT ('') FOR [FaxNumber]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_CustomText1]  DEFAULT ('') FOR [CustomText1]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_CustomText2]  DEFAULT ('') FOR [CustomText2]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_CustomText3]  DEFAULT ('') FOR [CustomText3]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_CustomText4]  DEFAULT ('') FOR [CustomText4]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_CustomText5]  DEFAULT ('') FOR [CustomText5]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_CustomNumber1]  DEFAULT (0) FOR [CustomNumber1]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_CustomNumber2]  DEFAULT (0) FOR [CustomNumber2]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_CustomNumber3]  DEFAULT (0) FOR [CustomNumber3]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_CustomNumber4]  DEFAULT (0) FOR [CustomNumber4]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_CustomNumber5]  DEFAULT (0) FOR [CustomNumber5]
GO

ALTER TABLE [dbo].[Supplier] ADD  CONSTRAINT [DF_OS_Supplier_Terms]  DEFAULT ('') FOR [Terms]
GO

ALTER TABLE [dbo].[Supplier] ADD  DEFAULT ('') FOR [Notes]
GO


/****** Object:  Table [dbo].[SupplierList]    Script Date: 10/10/2017 7:38:19 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[SupplierList](
	[MinimumOrder] [float] NOT NULL,
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ItemID] [int] NOT NULL,
	[SupplierID] [int] NOT NULL,
	[Cost] [money] NOT NULL,
	[ReorderNumber] [nvarchar](25) NOT NULL,
	[MasterPackQuantity] [int] NOT NULL,
	[DBTimeStamp] [timestamp] NULL,
	[TaxRate] [float] NOT NULL,
 CONSTRAINT [PK_SupplierList] PRIMARY KEY NONCLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[SupplierList] ADD  CONSTRAINT [DF_OS_SupplierList_MinimumOrder]  DEFAULT (0) FOR [MinimumOrder]
GO

ALTER TABLE [dbo].[SupplierList] ADD  CONSTRAINT [DF_OS_SupplierList_ItemID]  DEFAULT (0) FOR [ItemID]
GO

ALTER TABLE [dbo].[SupplierList] ADD  CONSTRAINT [DF_OS_SupplierList_SupplierID]  DEFAULT (0) FOR [SupplierID]
GO

ALTER TABLE [dbo].[SupplierList] ADD  CONSTRAINT [DF_OS_SupplierList_Cost]  DEFAULT (0) FOR [Cost]
GO

ALTER TABLE [dbo].[SupplierList] ADD  CONSTRAINT [DF_OS_SupplierList_ReorderNumber]  DEFAULT ('') FOR [ReorderNumber]
GO

ALTER TABLE [dbo].[SupplierList] ADD  CONSTRAINT [DF_OS_SupplierList_MasterPackQuantity]  DEFAULT (1) FOR [MasterPackQuantity]
GO

ALTER TABLE [dbo].[SupplierList] ADD  CONSTRAINT [DF_OS_SupplierList_TaxRate]  DEFAULT (0) FOR [TaxRate]
GO


/****** Object:  Table [dbo].[ItemTax]    Script Date: 10/10/2017 7:50:10 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[ItemTax](
	[HQID] [int] NOT NULL,
	[ID] [int] NOT NULL,
	[Description] [nvarchar](20) NOT NULL,
	[Options] [int] NOT NULL,
	[TaxID01] [int] NOT NULL,
	[ShowOnReceipt01] [bit] NOT NULL,
	[TaxID02] [int] NOT NULL,
	[ShowOnReceipt02] [bit] NOT NULL,
	[TaxID03] [int] NOT NULL,
	[ShowOnReceipt03] [bit] NOT NULL,
	[TaxID04] [int] NOT NULL,
	[ShowOnReceipt04] [bit] NOT NULL,
	[TaxID05] [int] NOT NULL,
	[ShowOnReceipt05] [bit] NOT NULL,
	[TaxID06] [int] NOT NULL,
	[ShowOnReceipt06] [bit] NOT NULL,
	[TaxID07] [int] NOT NULL,
	[ShowOnReceipt07] [bit] NOT NULL,
	[TaxID08] [int] NOT NULL,
	[ShowOnReceipt08] [bit] NOT NULL,
	[TaxID09] [int] NOT NULL,
	[ShowOnReceipt09] [bit] NOT NULL,
	[TaxID10] [int] NOT NULL,
	[ShowOnReceipt10] [bit] NOT NULL,
	[Code] [nvarchar](17) NOT NULL,
	[DBTimeStamp] [timestamp] NULL,
 CONSTRAINT [PK_ItemTax] PRIMARY KEY NONCLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_HQID]  DEFAULT (0) FOR [HQID]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_Description]  DEFAULT ('') FOR [Description]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_ApplyOnProfit]  DEFAULT (0) FOR [Options]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_TaxID01]  DEFAULT (0) FOR [TaxID01]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_ShowOnReceipt01]  DEFAULT (0) FOR [ShowOnReceipt01]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_TaxID02]  DEFAULT (0) FOR [TaxID02]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_ShowOnReceipt02]  DEFAULT (0) FOR [ShowOnReceipt02]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_TaxID03]  DEFAULT (0) FOR [TaxID03]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_ShowOnReceipt03]  DEFAULT (0) FOR [ShowOnReceipt03]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_TaxID04]  DEFAULT (0) FOR [TaxID04]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_ShowOnReceipt04]  DEFAULT (0) FOR [ShowOnReceipt04]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_TaxID05]  DEFAULT (0) FOR [TaxID05]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_ShowOnReceipt05]  DEFAULT (0) FOR [ShowOnReceipt05]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_TaxID06]  DEFAULT (0) FOR [TaxID06]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_ShowOnReceipt06]  DEFAULT (0) FOR [ShowOnReceipt06]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_TaxID07]  DEFAULT (0) FOR [TaxID07]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_ShowOnReceipt07]  DEFAULT (0) FOR [ShowOnReceipt07]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_TaxID08]  DEFAULT (0) FOR [TaxID08]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_ShowOnReceipt08]  DEFAULT (0) FOR [ShowOnReceipt08]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_TaxID09]  DEFAULT (0) FOR [TaxID09]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_ShowOnReceipt09]  DEFAULT (0) FOR [ShowOnReceipt09]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_TaxID10]  DEFAULT (0) FOR [TaxID10]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_ShowOnReceipt10]  DEFAULT (0) FOR [ShowOnReceipt10]
GO

ALTER TABLE [dbo].[ItemTax] ADD  CONSTRAINT [DF_OS_ItemTax_Code]  DEFAULT ('') FOR [Code]
GO

