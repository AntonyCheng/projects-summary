use proapi;

-- 接口信息
create table if not exists proapi.`interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '接口名称',
    `description` varchar(256) null comment '描述',
    `url` varchar(512) not null comment '接口地址',
    `requestParams` text not null comment '请求参数',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `status` int default 0 not null comment '接口状态0表示关闭，1表示开启',
    `method` varchar(256) not null comment '请求类型',
    `userId` bigint not null comment '创建人',
    `isDeleted` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '接口信息';

insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('苏峻熙', '余晟睿', 'www.anibal-mraz.net', '姜金鑫', '许乐驹', '陆烨霖', 7725418762, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('莫烨伟', '武涛', 'www.jackson-predovic.biz', '卢明辉', '廖熠彤', '黄明杰', 6373308, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('秦航', '邱鹏煊', 'www.george-white.co', '毛鹏涛', '武思远', '李伟泽', 5, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('叶立辉', '苏熠彤', 'www.bradly-boyer.net', '苏修洁', '戴立辉', '段博涛', 51981672, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('梁思源', '朱鹏', 'www.toney-keeling.net', '邹熠彤', '严文', '郝明杰', 58825799, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('郑擎宇', '任文博', 'www.nana-rippin.name', '余伟泽', '段弘文', '江智辉', 4609, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('莫思', '梁文昊', 'www.shelton-ernser.biz', '冯峻熙', '陆雪松', '周伟诚', 37200, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('阎修杰', '秦子轩', 'www.adolph-harvey.com', '钱弘文', '程思聪', '阎昊强', 49, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('汪思', '郭鹏煊', 'www.jeremy-kshlerin.org', '谭修杰', '黎钰轩', '杜耀杰', 4791, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('程浩', '陶鑫磊', 'www.margot-pouros.net', '吕煜祺', '徐立轩', '龙峻熙', 7048603, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('薛擎宇', '邵弘文', 'www.harriette-beahan.name', '陶钰轩', '龚文博', '龙昊焱', 61, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('傅志泽', '徐修洁', 'www.rudolph-legros.info', '熊博涛', '于烨伟', '钱雨泽', 45955, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('廖子轩', '洪鹏涛', 'www.jacquelin-rowe.co', '孔博文', '彭锦程', '张耀杰', 95, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('莫熠彤', '郑雪松', 'www.rodney-wintheiser.name', '金子涵', '黄弘文', '江熠彤', 301, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('梁浩', '侯瑞霖', 'www.valerie-jacobi.com', '曹炎彬', '彭凯瑞', '雷锦程', 851, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('朱哲瀚', '秦晟睿', 'www.theo-parisian.org', '郑绍辉', '邓昊强', '冯越彬', 152, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('钟泽洋', '田鸿煊', 'www.angila-hagenes.co', '江锦程', '熊致远', '秦鸿煊', 59994, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('高文轩', '陈彬', 'www.edmund-nitzsche.com', '董智辉', '宋嘉熙', '邹熠彤', 8, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('沈晟睿', '赖耀杰', 'www.bailey-roob.name', '莫荣轩', '傅立果', '江泽洋', 8397, 0);
insert into proapi.`interface_info` (`name`, `description`, `url`, `method`, `requestHeader`, `responseHeader`, `userId`, `status`) values ('马雨泽', '余昊焱', 'www.clifford-ortiz.name', '薛鹏', '郑立辉', '许楷瑞', 3630458, 0);


-- 用户调用接口关系
create table if not exists proapi.`user_interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `userId` bigint not null comment '调用者 id',
    `interfaceInfoId` bigint not null comment '接口 id',
    `totalNum` int default 0 not null comment '总调用次数',
    `leftNum` int default 0 not null comment '剩余调用次数',
    `status` int default 0 not null comment '0-表示正常，1-表示禁用',
    `isDeleted` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '用户调用接口关系';