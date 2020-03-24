CREATE DEFINER=`ext_app_user`@`223.229.240.53` PROCEDURE `jh_create_category`(in cate_code varchar(20), in cate_name varchar(255), in pare_id int(11), in current_datetime datetime)
BEGIN

    INSERT INTO `category`
	(`image`,
	`parent_id`,
	`top`,
	`column`,
	`sort_order`,
	`status`,
	`date_added`,
	`date_modified`,
	`category_name`,
	`category_code`)
	VALUES
	('',
	pare_id,
	0,
	1,
	9,
	1,
	current_datetime,
	current_datetime,
	cate_name,
	cate_code);

	select category_id from category where category_code = cate_code limit 0,1;

END