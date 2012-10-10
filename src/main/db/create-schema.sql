create table cities (
  id int not null,
  name varchar(100),
  height int,
  width int,
  left_max int,
  right_max int,
  top_max int,
  bottom_max int,
  hardcore bit default false,
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create table zones (
  id bigint not null auto_increment,
  x int not null,
  y int not null,
  zombies int,
  scout_sense int default -1,
  visited bit default false,
  zone_depleted bit default false,
  building_depleted bit default false,
  blue_print_retrieved bit default false,
  scout_peek varchar(100),
  camping_topology varchar(15) not null default 'UNKNOWN',
  city_id bigint,
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
alter table zones add constraint fk_zones_cities_city_id foreign key (city_id) references cities(id);
create index idx_zones_city_x_y on zones (city_id,x,y);
alter table zones add constraint unique_zones_city_id_x_y unique (city_id,x,y);

create table items (
  id bigint not null auto_increment,
  amount int,
  broken bit default false,
  d2n_item_id int,
  zone_id bigint,
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
alter table items add constraint fk_items_zones_zone_id foreign key (zone_id) references zones(id);
alter table items add constraint unique_items_zone_item_broken unique (zone_id, d2n_item_id, broken);

create table unique_items (
  id int not null,
  name varchar(100),
  image varchar(30),
  in_sprite bit default false,
  poisoned bit default false,
  preset_bp bit default false,
  breakable bit default false,
  category varchar(20),
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create table users (
  id int not null auto_increment,
  city_id int not null,
  user_key varchar(50),
  name varchar(100),
  secure bit default false,
  shunned bit default false,
  banned bit default false,
  soul_points int default 0,
  game_id int default null,
  image varchar(30),
  description varchar(200),
  primary key(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
create index idx_users_user_key on users (user_key);
create index idx_users_name on users (name);
create index idx_users_game_id on users (game_id);
alter table users add constraint fk_users_cities_city_id foreign key (city_id) references cities(id);

create table user_activity (
  id int not null auto_increment,
  action varchar(20) not null,
  updated datetime not null,
  zone_id bigint,
  user_id bigint not null,
  city_id bigint not null,
  primary key(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
alter table user_activity add constraint fk_user_activity_users_user_id foreign key (user_id) references users(id);
alter table user_activity add constraint fk_user_activity_cities_city_id foreign key (city_id) references cities(id);
alter table user_activity add constraint fk_user_activity_zones_zone_id foreign key (zone_id) references zones(id);
create index idx_user_activity_updated on user_activity(updated);
create index idx_user_activity_city_user_updated on user_activity (city_id, user_id, updated);

create table unique_outside_buildings (
  id int not null,
  name varchar(100) not null,
  flavor varchar(1024),
  url varchar(200),
  primary key(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create table unique_inside_buildings (
  id int not null,
  name varchar(100) not null,
  temporary bit default false,
  parent int,
  image varchar(20),
  in_sprite bit default false,
  always_available bit default false,
  flavor varchar(1024),
  url varchar(200),
  primary key(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create table last_user_activity (
  zone_id bigint not null,
  user_activity_id int not null
) ENGINE=INNODB DEFAULT CHARSET=utf8;
alter table last_user_activity add constraint fk_last_user_activity_user_activity_user_activity_id foreign key (user_activity_id) references user_activity(id);
alter table last_user_activity add constraint fk_last_user_activity_zones_zone_id foreign key (zone_id) references zones(id);

create table inside_buildings (
  id int not null auto_increment,
  status varchar(20),
  unique_inside_building_id int not null,
  city_id int not null,
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
alter table inside_buildings add constraint fk_inside_buildings_cities_city_id foreign key (city_id) references cities(id);
alter table inside_buildings add constraint fk_inside_buildings_uib_unique_inside_building_id foreign key (unique_inside_building_id) references unique_inside_buildings(id);
alter table inside_buildings add constraint unique_inside_buildings_city_building unique (city_id, unique_inside_building_id);

create table unique_distinctions (
  id int not null auto_increment,
  name varchar(40) not null,
  rare bit default false,
  image varchar(20) not null,
  in_sprite bit default false,
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create table distinctions (
  id int not null auto_increment,
  user_id int not null,
  unique_distinction_id int not null,
  amount int,
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
alter table distinctions add constraint fk_distinctions_users_user_id foreign key (user_id) references users(id);
alter table distinctions add constraint fk_distinctions_unique_distinctions_unique_distinction_id foreign key (unique_distinction_id) references unique_distinctions(id);
alter table distinctions add constraint unique_distinctions_user_distinction unique (user_id, unique_distinction_id);

create table rooms (
  id int not null auto_increment,
  zone_id bigint not null,
  x int not null,
  y int not null,
  door varchar(8) not null default 'NONE',
  door_key varchar(15) not null default 'UNKNOWN',
  zombies int not null,
  corridor_west bit default false,
  corridor_north bit default false,
  corridor_east bit default false,
  corridor_south bit default false,
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
alter table rooms add constraint fk_rooms_zones_zone_id foreign key (zone_id) references zones(id);
alter table rooms add constraint unique_rooms_zone_x_y unique (zone_id, x, y);
create index idx_rooms_zone_x_y on rooms(zone_id, x, y);
