-- declare @itemqty2sync table (id int, code nvarchar(255), qty int);
-- insert into @itemqty2sync
-- select i2.ID, i1.ItemLookupCode, i1.Quantity from Beta.dbo.Item i1 inner join Temp.dbo.Item i2 on i1.ItemLookupCode = i2.ItemLookupCode;

-- update Temp.dbo.Item set Quantity = qty from Temp.dbo.Item i inner join @itemqty2sync u on i.ID = u.id;

update Temp.dbo.Item set Quantity = i2.Quantity from Temp.dbo.Item i1 inner join Beta.dbo.Item i2 on i2.ItemLookupCode = i1.ItemLookupCode;