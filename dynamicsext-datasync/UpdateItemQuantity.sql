/* Queries to decrease/increase quantity of Items in Primary DB with the transactions created in Secondary DB after last executed date */
/* START */

USE Beta;

if OBJECT_ID(N'dbo.[dynamicsext_lastrun_status]', N'U') is null
begin
	CREATE TABLE [dbo].[dynamicsext_lastrun_status](
	[PROCESS_NAME] [nvarchar](250) NOT NULL,
	[LAST_RUN_ON] [datetime] NOT NULL,
		CONSTRAINT [PK_dynamicsext_lastrun_status] PRIMARY KEY CLUSTERED 
	(
		[PROCESS_NAME] ASC
	)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
	) ON [PRIMARY]
end

declare @lastrunon datetime, @processname nvarchar(255), @currentdate datetime;

set @processname = 'UpdateItemQuantity';
set @lastrunon = (select [LAST_RUN_ON] from [dynamicsext_lastrun_status] where PROCESS_NAME = @processname);
set @currentdate = CURRENT_TIMESTAMP;

if (@lastrunon is null) begin
	set @lastrunon = '2016-01-01';
end

declare @updateqty table (code nvarchar(255), qty int);


USE Temp;

insert into @updateqty
select 
	ItemLookupCode, 
	sum(t.quantity) as qty 
from dbo.TransactionEntry t 
	inner join dbo.Item i 
		on i.ID = t.ItemID 
where TransactionTime between @lastrunon and @currentdate
group by ItemLookupCode;


USE Beta;

select * from @updateqty;
	
select ItemLookupCode, Quantity from Item i inner join @updateqty u on i.ItemLookupCode = u.code;

update Item set Quantity = Quantity - qty from Item i inner join @updateqty u on i.ItemLookupCode = u.code;

select ItemLookupCode, Quantity from Item i inner join @updateqty u on i.ItemLookupCode = u.code;

set @currentdate = DATEADD(ss, 1, @currentdate);

merge [dynamicsext_lastrun_status] as t 
using (select @processname as PROCESS_NAME) as s
on t.PROCESS_NAME = s.PROCESS_NAME
when matched then update set [LAST_RUN_ON] = @currentdate
when not matched then insert (PROCESS_NAME, [LAST_RUN_ON]) values (s.PROCESS_NAME, @currentdate);

/* END */