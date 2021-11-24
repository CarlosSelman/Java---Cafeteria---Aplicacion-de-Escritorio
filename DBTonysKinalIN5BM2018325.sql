drop database if exists DBTonysKinal2018325;
create database DBTonysKinal2018325; 
use DBTonysKinal2018325;
# ================================================================================================================= #
# -------------------------------------------------sp_Crear-------------------------------------------------------- #
# ================================================================================================================= #
-- --------------------------------------------------------------------------------------------------------------- --
-- 1) TipoEmpleado
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$ 
create procedure sp_CrearTipoEmpleado()
begin
	create table TipoEmpleado(codigoTipoEmpleado int auto_increment not null primary key, 
	descripcion varchar(100));
end $$
delimiter ;
call sp_CrearTipoEmpleado();
-- --------------------------------------------------------------------------------------------------------------- --
-- 2) Empleados
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_CrearEmpleados()
begin
start transaction;
	create table Empleados(codigoEmpleado int auto_increment not null primary key,
	numeroEmpleado int,
	apellidosEmpleado varchar(150),
	nombresEmpleado varchar(150),
	direccionEmpleado varchar(150),
	telefonoContacto varchar(10),
	gradoCocinero varchar(50),
	codigoTipoEmpleado int,
    foreign key (codigoTipoEmpleado) references TipoEmpleado(codigoTipoEmpleado) on delete cascade);
commit;
end $$
delimiter ;
call sp_CrearEmpleados();
-- --------------------------------------------------------------------------------------------------------------- --
-- 3) Empresas
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$ 
create procedure sp_CrearEmpresas()
begin
	create table Empresas(codigoEmpresa int auto_increment not null primary key, 
	nombreEmpresa varchar(150),
	direccion varchar(150),
	telefono varchar(10));
end $$
delimiter ;
call sp_CrearEmpresas();
-- --------------------------------------------------------------------------------------------------------------- --
-- 4) Servicios
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$ 
create procedure sp_CrearServicios()
begin
	create table Servicios(codigoServicio int auto_increment not null primary key,
	fechaServicio date,
	tipoServicio varchar(100),
	horaServicio time,
	lugarServicio varchar(100),
	telefonoContacto varchar(10),
	codigoEmpresa int,
	foreign key (codigoEmpresa) references Empresas(codigoEmpresa) on delete cascade);
commit;
end $$
delimiter ;
call sp_CrearServicios();
-- --------------------------------------------------------------------------------------------------------------- --
-- 5) Servicios_has_Empleados
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$ 
create procedure sp_CrearServicios_has_Empleados()
begin
	create table Servicios_has_Empleados(
	codigoServicio int,
	codigoEmpleado int,
	fechaEvento date,
	horaEvento time,
	lugarEvento varchar(150),
	foreign key (codigoServicio) references Servicios(codigoServicio) on delete cascade,
	foreign key (codigoEmpleado) references Empleados(codigoEmpleado) on delete cascade);
end $$
delimiter ;
call sp_CrearServicios_has_Empleados();
-- --------------------------------------------------------------------------------------------------------------- --
-- 6) Presupuesto
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$ 
create procedure sp_CrearPresupuesto()
begin
	create table Presupuesto(codigoPresupuesto int auto_increment not null primary key, 
	fechaSolicitud date,
	cantidadPresupuesto decimal(10,2),
	codigoEmpresa int,
	foreign key (codigoEmpresa) references Empresas(codigoEmpresa) on delete cascade);
end $$
delimiter ;
call sp_CrearPresupuesto();
-- --------------------------------------------------------------------------------------------------------------- --
-- 7) TipoPlato
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$ 
create procedure sp_CrearTipoPlato()
begin
	create table TipoPlato(codigoTipoPlato int auto_increment not null primary key,
	descripcionTipo varchar(100));
end $$
delimiter ;
call sp_CrearTipoPlato();
-- --------------------------------------------------------------------------------------------------------------- --
-- 8) Platos
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$ 
create procedure sp_CrearPlatos()
begin
	create table Platos(codigoPlato int primary key auto_increment,
	cantidad int,
	nombrePlato varchar(50),
	descripcionPlato varchar(150),
	precioPlato decimal(10,2),
	codigoTipoPlato int,
	foreign key (codigoTipoPlato) references TipoPlato(codigoTipoPlato) on delete cascade);
end $$
delimiter ;
call sp_CrearPlatos();
-- --------------------------------------------------------------------------------------------------------------- --
-- 9) Servicios_has_Platos
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$ 
create procedure sp_CrearServicios_has_Platos()
begin
	create table Servicios_has_Platos(Servicios_codigoServicio int,
	Platos_codigoPlato int,
	foreign key (Servicios_codigoServicio) references Servicios(codigoServicio)on delete cascade,
	foreign key (Platos_codigoPlato) references Platos(codigoPlato) on delete cascade);
end $$
delimiter ;
call sp_CrearServicios_has_Platos();
-- --------------------------------------------------------------------------------------------------------------- --
-- 10) Productos
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$ 
create procedure sp_CrearProductos()
begin
	create table Productos(codigoProducto int auto_increment not null primary key,
	nombreProducto varchar(150),
	cantidad int);
end $$
delimiter ;
call sp_CrearProductos();
-- --------------------------------------------------------------------------------------------------------------- --
-- 11) Productos_has_Platos
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$ 
create procedure sp_CrearProductos_has_Platos()
begin
	create table Productos_has_Platos(Productos_codigoProducto int,
	Platos_codigoPlato int,
	foreign key (Productos_codigoProducto) references Productos(codigoProducto) on delete cascade,
	foreign key (Platos_codigoPlato) references Platos(codigoPlato) on delete cascade);
end $$
delimiter ;
call sp_CrearProductos_has_Platos();
-- --------------------------------------------------------------------------------------------------------------- --
# ================================================================================================================= #
# -------------------------------------------------sp_Agregar------------------------------------------------------ #
# ================================================================================================================= #
-- --------------------------------------------------------------------------------------------------------------- --
-- 1) TipoEmpleado
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_AgregarTipoEmpleado(in d varchar(100))
begin
start transaction;
  insert into TipoEmpleado(descripcion) values (d);
commit;
end $$
delimiter ;
call sp_AgregarTipoEmpleado('Recepcionista');
call sp_AgregarTipoEmpleado('Administrador');
call sp_AgregarTipoEmpleado('Chef');
call sp_AgregarTipoEmpleado('Mesero');
call sp_AgregarTipoEmpleado('Gastrónomo');
call sp_AgregarTipoEmpleado('Catador');
call sp_AgregarTipoEmpleado('Ayudante de Camarero');
-- --------------------------------------------------------------------------------------------------------------- --
-- 2) Empleados
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_AgregarEmpleados(in nuE int,in apE varchar(150),in nomE varchar(150),
in dirE varchar(150),in telC varchar(10), in graC varchar(50),in codTiE int)
begin
  insert into Empleados(numeroEmpleado,apellidosEmpleado,nombresEmpleado,direccionEmpleado,
  telefonoContacto,gradoCocinero,codigoTipoEmpleado) values (nuE,apE,nomE,dirE,telC,graC,codTiE);
end $$
delimiter ;
call sp_AgregarEmpleados(301,'Méndez Ordoñez','Juan Antonio','Guatemala,Zona 7',25879658,'Director de alimentos y bebidas',3);
call sp_AgregarEmpleados(302,'Loarca Jiménez','Luis Carlos','Guatemala,Zona 7',59694584,'Director de producción',3);
call sp_AgregarEmpleados(303,'Vargas Véliz','Luis Miguel','Guatemala,Zona 7',98675838,'Director de restauración o banquetes',3);
call sp_AgregarEmpleados(304,'Girón Felix','Marco Aurelio','Guatemala,Zona 11',49596876,'Jefe de operaciones en catering',3);
call sp_AgregarEmpleados(305,'Soto Chum','Leonel Luis','Guatemala,Zona 11',43536476,'Ayudante de sommelier',3);
call sp_AgregarEmpleados(306,'Navas Choc','Angel Gabriel','Guatemala,Zona 11',23454321,'Jefe de compras',3);
call sp_AgregarEmpleados(307,'Ochoa de León','Obed Alejandro','Guatemala,Zona 1',68775835,'Jefe de cocina',3);
call sp_AgregarEmpleados(308,'Ruíz Escobar','Mario Otoniel','Guatemala,Zona 1',98765432,'Jefe de sala',3);
call sp_AgregarEmpleados(309,'Poj Ortíz','Oscar Emilio','Guatemala,Zona 1',12345678,'Responsable de bar/cafetería',3);
call sp_AgregarEmpleados(310,'Polanco Azurdia','Luis Gabriel','Guatemala,Zona 1',12345623,'Responsable de economato y bodega',3);
call sp_AgregarEmpleados(101,'Arreola Paz','Pamela Andrea','Guatemala,Zona 4',98754689,'N/A',1);
call sp_AgregarEmpleados(201,'Posada Esquivel','Ricardo Alfonso','Guatemala,Zona 4',48658692,'N/A',2);
call sp_AgregarEmpleados(401,'Navas Mayen','Marta Gabriela','Guatemala,Zona 4',10293847,'N/A',4);
call sp_AgregarEmpleados(501,'Choc Polanco','Marlene Daniela','Guatemala,Zona 6',09876543,'N/A',5);
call sp_AgregarEmpleados(601,'Chup Rox','Andrés Alexander','Guatemala,Zona 6',97685421,'N/A',6);
call sp_AgregarEmpleados(701,'Koke Ruí','Luisa Ana','Guatemala,Zona 6',97531426,'N/A',7);
-- --------------------------------------------------------------------------------------------------------------- --
-- 3) Empresas
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_AgregarEmpresas(in nE varchar(150),in dir varchar(150),in tel varchar(10))
begin
  insert into Empresas(nombreEmpresa,direccion,telefono) values (nE,dir,tel);
end $$
delimiter ;
call sp_AgregarEmpresas('INTEK','10-31, 4A Avenida, Guatemala','2507-0500');
call sp_AgregarEmpresas('Envases Plásticos | Lacoplast - Polytec','8 Av 30-40, Guatemala 01011','6671-8300');
call sp_AgregarEmpresas('Frascos de Vidrio en Guate, Corporación Nash','5 Calle 1-37, Guatemala 01013','5951 6435');
call sp_AgregarEmpresas('ALIMENTOS NUTRICIONALES DE C.A. S.A','6a. AVENIDA 3-28 ZONA 2 SAN JOSE VILLA NUEVA GUATEMALA, Villa Nueva','6630 1290');
call sp_AgregarEmpresas('Ilgua, S.A.','8A Avenida, Guatemala','2311-1700');
call sp_AgregarEmpresas('Industrias Alimenticias Kern´s & cia. sca.','Kilómetro 7 carreteral al Atlántico, zona 18 Guatemala. ','2323-7100');
-- --------------------------------------------------------------------------------------------------------------- --
-- 4) Servicios
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_AgregarServicios(in FS date,in TS varchar(100),in HS varchar(100),in LS varchar(100),
in TC varchar(10),in CE int)
begin
   insert into Servicios(fechaServicio,tipoServicio,horaServicio,lugarServicio,
   telefonoContacto,codigoEmpresa) values (FS,TS,HS,LS,TC,CE);
end $$
delimiter ;
call sp_AgregarServicios('2020/03/28','Servicio Americano','07:30:00','10-31, 4A Avenida, Guatemala','2507-0500',1);
call sp_AgregarServicios('2020/03/29','Servicio Francés','07:30:00','8 Av 30-40, Guatemala 01011','6671-8300',2);
call sp_AgregarServicios('2020/03/27','Servicio a la Rusa','07:30:00','5 Calle 1-37, Guatemala 01013','5951 6435',3);
call sp_AgregarServicios('2020/03/26','Servicio Inglés','07:30:00','6a. AVENIDA 3-28 ZONA 2 SAN JOSE VILLA NUEVA GUATEMALA, Villa Nueva','6630 1290',4);
call sp_AgregarServicios('2020/03/25','Servicio gueridón','08:30:00','8A Avenida, Guatemala','2311-1700',5);
call sp_AgregarServicios('2020/03/28','Servicio buffet','08:30:00','Kilómetro 7 carreteral al Atlántico, zona 18 Guatemala. ','2323-7100',6);
call sp_AgregarServicios('2020/03/29','Servicio Americano','08:30:00','10-31, 4A Avenida, Guatemala','2507-0500',1);
call sp_AgregarServicios('2020/03/30','Servicio Francés','09:30:00','8 Av 30-40, Guatemala 01011','6671-8300',2);
call sp_AgregarServicios('2020/03/30','Servicio a la Rusa','10:30:00','5 Calle 1-37, Guatemala 01013','5951 6435',3);
call sp_AgregarServicios('2020/03/16','Servicio Inglés','11:30:00','6a. AVENIDA 3-28 ZONA 2 SAN JOSE VILLA NUEVA GUATEMALA, Villa Nueva','6630 1290',4);
call sp_AgregarServicios('2020/03/17','Servicio gueridón','12:30:00','8A Avenida, Guatemala','2311-1700',5);
call sp_AgregarServicios('2020/03/19','Servicio buffet','13:30:00','Kilómetro 7 carreteral al Atlántico, zona 18 Guatemala. ','2323-7100',6);
-- --------------------------------------------------------------------------------------------------------------- --
-- 5) Servicios_has_Empleados
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_AgregarServicios_has_Empleados(in SCS int,in ECE int,in FE date,in HE time,in LE varchar(150))
begin
  insert into Servicios_has_Empleados(codigoServicio,codigoEmpleado,fechaEvento,
  horaEvento,lugarEvento) values (SCS,ECE,FE,HE,LE);
end $$
delimiter ;
call sp_AgregarServicios_has_Empleados(1,1,'2020/03/28','07:30:00','10-31, 4A Avenida, Guatemala');
call sp_AgregarServicios_has_Empleados(2,2,'2020/03/29','07:30:00','8 Av 30-40, Guatemala 01011');
call sp_AgregarServicios_has_Empleados(3,3,'2020/03/27','07:30:00','5 Calle 1-37, Guatemala 01013');
call sp_AgregarServicios_has_Empleados(4,1,'2020/03/26','07:30:00','6a. AVENIDA 3-28 ZONA 2 SAN JOSE VILLA NUEVA GUATEMALA, Villa Nueva');
call sp_AgregarServicios_has_Empleados(5,2,'2020/03/25','08:30:00','8A Avenida, Guatemala');
call sp_AgregarServicios_has_Empleados(6,3,'2020/03/28','08:30:00','Kilómetro 7 carreteral al Atlántico, zona 18 Guatemala.');
call sp_AgregarServicios_has_Empleados(1,1,'2020/03/29','08:30:00','10-31, 4A Avenida, Guatemala');
call sp_AgregarServicios_has_Empleados(2,2,'2020/03/30','09:30:00','8 Av 30-40, Guatemala 01011');
call sp_AgregarServicios_has_Empleados(3,3,'2020/03/30','10:30:00','5 Calle 1-37, Guatemala 01013');
call sp_AgregarServicios_has_Empleados(4,1,'2020/03/16','11:30:00','6a. AVENIDA 3-28 ZONA 2 SAN JOSE VILLA NUEVA GUATEMALA, Villa Nueva');
call sp_AgregarServicios_has_Empleados(5,2,'2020/03/17','12:30:00','8A Avenida, Guatemala');
call sp_AgregarServicios_has_Empleados(6,3,'2020/03/19','13:30:00','Kilómetro 7 carreteral al Atlántico, zona 18 Guatemala.');
-- --------------------------------------------------------------------------------------------------------------- --
-- 6) Presupuesto
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_AgregarPresupuesto(in fs date,in cp decimal(10,2),in ce int)
begin
  insert into Presupuesto(fechaSolicitud,cantidadPresupuesto,codigoEmpresa) values (fs,cp,ce);
end $$
delimiter ;
call sp_AgregarPresupuesto('2020/02/25','3000','1');
call sp_AgregarPresupuesto('2020/02/24','3000','2');
call sp_AgregarPresupuesto('2020/02/23','3500','3');
call sp_AgregarPresupuesto('2020/02/22','4000','4');
call sp_AgregarPresupuesto('2020/02/21','4500','5');
call sp_AgregarPresupuesto('2020/02/20','5000','6');
call sp_AgregarPresupuesto('2020/02/26','4500','1');
call sp_AgregarPresupuesto('2020/02/27','4000','2');
call sp_AgregarPresupuesto('2020/02/28','3500','3');
call sp_AgregarPresupuesto('2020/02/15','3000','4');
call sp_AgregarPresupuesto('2020/02/16','3000','5');
call sp_AgregarPresupuesto('2020/02/14','3000','6');
-- --------------------------------------------------------------------------------------------------------------- --
-- 7) TipoPlato
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_AgregarTipoPlato(in dT varchar(100))
begin
  insert into TipoPlato(descripcionTipo) values (dT);
end $$
delimiter ;
call sp_AgregarTipoPlato('Comida Oriental');
call sp_AgregarTipoPlato('Comida Italiana');
call sp_AgregarTipoPlato('Comida Americana');
call sp_AgregarTipoPlato('Comida Árabe');
-- --------------------------------------------------------------------------------------------------------------- --
-- 8) Platos
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_AgregarPlatos(in c int,in nP varchar(50),in dP varchar(150), in pP decimal(10,2),in cTp int)
begin
  insert into Platos(cantidad,nombrePlato,descripcionPlato,precioPlato,codigoTipoPlato) values (c,nP,dP,pP,cTp);
end $$
delimiter ;
call sp_AgregarPlatos(1,'CERDO AGRIDULCE','Es de color naranja-rojo brillante y su sabor es entre dulce y amargo.',30,1);
call sp_AgregarPlatos(1,'POLLO GONG BAO O KUNG PAO','Es una especialidad de Sichuan y los ingredientes principales son pollo en cubos, chile seco y cacahuates fritos.',30,1);
call sp_AgregarPlatos(1,'MA PO TOFU','Su sabor picante viene del polvo de pimienta, un tipo de condimento muy utilizado en la cocina de esta región.',30,1);
call sp_AgregarPlatos(1,'WONTON','Tienen forma de saquitos y comúnmente se sirven en sopa hervidos,aunque también pueden ser fritos, rellenos de cerdo o camarón. ',30,1);
call sp_AgregarPlatos(1,'CHOW MEIN','Son fideos fritos con carne de pollo, res, camarones, o cerdo, cocinados con cebolla y apio.',30,1);
call sp_AgregarPlatos(1,'DUMPLINGS','Una especie de empanaditas de masa de harina de trigo rellenas de carne picada de cerdo, pollo, res, camarones y verduras.',30,1);
call sp_AgregarPlatos(1,'PATO PEKÍN','Pato asado es de lo más famoso de la capital china, Pekín, apreciado por su piel fina y crujiente, y la carne muy suave y jugosa. ',30,1);
call sp_AgregarPlatos(1,'ROLLITOS PRIMAVERA','Son entremeses cantoneses de forma cilíndrica y rellenos de verduras o carne, de sabor dulce o salado.',30,1);
call sp_AgregarPlatos(1,'LASAÑA','Entre lámina y lámina también lleva bechamel, y para terminar abundante queso con el que se le dará su aspecto habitual gratinado.',30,2); 
call sp_AgregarPlatos(1,'RISOTTO','Esta comida típica italiana se prepara a base de arroz y de queso parmesano.',30,2);
call sp_AgregarPlatos(1,'CARPACCIO','El Carpaccio es actualmente una receta famosa a nivel internacional,que pone de moda eso de comer carne cruda. ',30,2);
call sp_AgregarPlatos(1,'SOPA MINESTRONE','Esta especialidad de la cocina italiana es similar a una sopa de verduras,que también contiene pasta y en algunas recetas carne, pollo o jamón. ',30,2);
call sp_AgregarPlatos(1,'PIZZA','Pan Italiano sabroso en porciones con varios ingredientes.',30,2);
call sp_AgregarPlatos(1,'ENSALADA CAPRESSE','Ensalada deliciosa con tomate, mozzarella fresca, unas hojas de albahaca y un chorrito de aceite de oliva. ',30,2);
call sp_AgregarPlatos(1,'OSSOBUCO','El ossobuco es un plato de carne tradicional de Milán que se trata de un estofado de jarrete de ternera sin deshuesar.',30,2);
call sp_AgregarPlatos(1,'ESPAGUETIS A LA CARBONARA','Si quieres hacer un plato fácil y típico de la comida italiana no dudes en preparar unos ricos espaguetis a la carbonara.',30,2);
call sp_AgregarPlatos(1,'BBQ Ribs','Las costillas de cerdo al horno al estilo americano, o también hechas en la barbacoa, son un clásico de la gastronomía americana.',30,3);
call sp_AgregarPlatos(1,'HAMBURGUESA','Unos panecillos caseros, una carne hecha justo a su punto y muchas ideas para la guarnición, son las claves. ',30,3);
call sp_AgregarPlatos(1,'SPAGHETTI MEAT BALLS','En este caso se trata de una receta que se cocina la carne en el horno y se coloca con la salsa sobre los espaguetis.',30,3);
call sp_AgregarPlatos(1,'MAC&CHEESE','Macarrones con queso gratinados.',30,3);
call sp_AgregarPlatos(1,'ALITAS AL ESTILO BUFFALO','Alitas de pollo picantes al estilo Buffalo. ',30,3);
call sp_AgregarPlatos(1,'COLESLAW','La ensalada americana de col o coleslaw es la guarnición perfecta para casi todas las recetas de carne americanas.',30,3);
call sp_AgregarPlatos(1,'HUMMUS','Es una crema de garbanzos, tahini, limón y aceite de oliva.',30,4);
call sp_AgregarPlatos(1,'KIBBE','Este alimento que se asemeja a una albóndiga es común en Oriente Medio,, se elabora con carne picada de cordero, sémola de trigo y especias.',30,4);
call sp_AgregarPlatos(1,'CUSCÚS','Es hecho a base de sémola de trigo que se mezcla con un estofado de carne con verduras elaborado en una vaporera.',30,4);
call sp_AgregarPlatos(1,'FALAFEL','Se trata de un perfecto entrante que normalmente se acompaña con salsa de yogur. ',30,4);
call sp_AgregarPlatos(1,'MAQLUBA','Se podría decir que es la paella árabe, ya que se elabora principalmente con arroz, que adquiere un tono amarillento.',30,4);
call sp_AgregarPlatos(1,'DONER','Consiste en finas láminas de cordero, pollo o ternera cocinado verticalmente que se come en pan de pita o durum junto a vegetales y salda de yogur. ',30,4);
call sp_AgregarPlatos(1,'BAKLAVA','Y de postre este pastel elaborado con nueces trituradas, masa filo y almíbar. El sabor dulzón de oriente que se suele tomar junto con un té.',30,4);
-- --------------------------------------------------------------------------------------------------------------- --
-- 9) Servicios_has_Platos
-- --------------------------------------------------------------------------------------------------------------- --
-- --------------------------------------------------------------------------------------------------------------- --
-- 10) Productos
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_AgregarProductos(in nP varchar(150),in c int)
begin
  insert into Productos(nombreProducto,cantidad) values (nP,c);
end $$
delimiter ;
call sp_AgregarProductos("Variedad de Carnes (De Pollo,Res,Cerdo y Pato) - LB",50);
call sp_AgregarProductos("Variedad de Frutas y Verduras - LB",100);
call sp_AgregarProductos("Variedad de Lácteos (Cremas,Quesos y Leche) - LB",100);
call sp_AgregarProductos("Variedad Aceites(Girasol,Oliva y Extra Virgen) - Botella",45);
call sp_AgregarProductos("Harinas - LB",60);
call sp_AgregarProductos("Pastas Italianas y Orientales - Paquete",100);
call sp_AgregarProductos("Huevos - Uds",1000);
call sp_AgregarProductos("Hierbas (Albahaca,Comino,Pereji,Orégano...) - Manojo",45);
call sp_AgregarProductos("Panes(Rallado,Duro,Hamburguesa...) - Caja",45);
call sp_AgregarProductos("Salsas y Aderesos(Americanos,Italianos y Orientales - Bote",60);
call sp_AgregarProductos("Condimentos Orientales,Italianos,Mediterráneos(Sal,Azafrán,Thine...) - Sobre",120);
call sp_AgregarProductos("Arroz y Frijol -LB",120);
-- --------------------------------------------------------------------------------------------------------------- --
-- 11) Productos_has_Platos
-- --------------------------------------------------------------------------------------------------------------- --
-- --------------------------------------------------------------------------------------------------------------- --
# ================================================================================================================= #
# -----------------------------------------------sp_Actualizar----------------------------------------------------- #
# ================================================================================================================= #
-- --------------------------------------------------------------------------------------------------------------- --
-- 1) TipoEmpleado
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_ActualizarTipoEmpleado(in d varchar(100),in cTe int)
begin
	update TipoEmpleado set descripcion=d where codigoTipoEmpleado=cTe;
end $$
delimiter ;
call sp_ActualizarTipoEmpleado('R',1); -- Modificando_Id_1
call sp_ActualizarTipoEmpleado('Recepcionista',1); -- Restaurando_Id_1
-- --------------------------------------------------------------------------------------------------------------- --
-- 2) Empleados
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_ActualizarEmpleados(in nuE int,in apE varchar(150),in nomE varchar(150),
in dirE varchar(150),in telC varchar(10), in graC varchar(50),in iDmE int)
begin
	update Empleados set numeroEmpleado=nuE,apellidosEmpleado=apE,nombresEmpleado=nomE,direccionEmpleado=dirE,
	telefonoContacto=telC,gradoCocinero=graC where codigoEmpleado=iDmE;
end $$
delimiter ;
call sp_ActualizarEmpleados(301,'M O','J A','G,Z 7',25879658,'DAyB',1); -- Modificando_Id_1
call sp_ActualizarEmpleados(301,'Méndez Ordoñez','Juan Antonio','Guatemala,Zona 7',25879658,'Director de alimentos y bebidas',1); -- Restaurando_Id_1
-- --------------------------------------------------------------------------------------------------------------- --
-- 3) Empresas
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_ActualizarEmpresas(in nE varchar(150),in dir varchar(150),in tel varchar(10),in cE int)
begin
	update Empresas set nombreEmpresa=nE,direccion=dir,telefono=tel where codigoEmpresa=cE;
end $$
delimiter ;
call sp_ActualizarEmpresas('INTEK','GTML','2507-0500',1); -- Modificando_Id_1
call sp_ActualizarEmpresas('INTEK','10-31, 4A Avenida, Guatemala','2507-0500',1); -- Restaurando_Id_1
-- --------------------------------------------------------------------------------------------------------------- --
-- 4) Servicios
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_ActualizarServicios(in FS date,in TS varchar(100),in HS varchar(100),in LS varchar(100),
in TC varchar(10),in cS int)
begin
	update Servicios set fechaServicio=FS,tipoServicio=TS,horaServicio=HS,lugarServicio=LS,telefonoContacto=TC where codigoServicio=cS;
end $$
delimiter ;
call sp_ActualizarServicios('2020/03/28','S A','02:30:00','Guatemala','2507-0500',1); -- Modificando_Id_1
call sp_ActualizarServicios('2020/03/28','Servicio Americano','02:30:00','10-31, 4A Avenida, Guatemala','2507-0500',1);  -- Restaurando_Id_1
-- --------------------------------------------------------------------------------------------------------------- --
-- 5) Servicios_has_Empleados
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_ActualizarServicios_has_Empleados(in FE date,in HE time,in LE varchar(150),in cShE int,in cEcE int)
begin
	update Servicios_has_Empleados set fechaEvento=FE,horaEvento=HE,lugarEvento=LE
	where codigoServicio=cShE and codigoEmpleado=cEcE;
end $$
delimiter ;
call sp_ActualizarServicios_has_Empleados('2020/03/28','07:30:00','Guatemala',1,1); -- Modificando_Id_1
call sp_ActualizarServicios_has_Empleados('2020/03/28','07:30:00','10-31, 4A Avenida, Guatemala',1,1); -- Restaurando_Id_1
-- --------------------------------------------------------------------------------------------------------------- --
-- 6) Presupuesto
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_ActualizarPresupuesto(in fs date,in cp decimal(10,2),in cPsTo int)
begin
	update Presupuesto set fechaSolicitud=fs,cantidadPresupuesto=cp where codigoPresupuesto=cPsTo;
end $$
delimiter ;
call sp_ActualizarPresupuesto('2020/02/25','2501',1); -- Modificando_Id_1
call sp_ActualizarPresupuesto('2020/02/25','2500',1); -- Restaurando_Id_1
-- --------------------------------------------------------------------------------------------------------------- --
-- 7) TipoPlato
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_ActualizarTipoPlato(in dT varchar(100),in cTp int)
begin
	update TipoPlato set descripcionTipo=dT  where codigoTipoPlato=cTp;
end $$
delimiter ;
call sp_ActualizarTipoPlato('C.O',1); -- Modificando_Id_1
call sp_ActualizarTipoPlato('Comida Oriental',1); -- Restaurando_Id_1
-- --------------------------------------------------------------------------------------------------------------- --
-- 8) Platos
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_ActualizarPlatos(in c int,in nP varchar(50),in dP varchar(150), in pP decimal(10,2),in CaP int)
begin
	update Platos set cantidad=c,nombrePlato=nP,descripcionPlato=dP,precioPlato=pP where codigoPlato=CaP;
end $$
delimiter ;
call sp_ActualizarPlatos(1,'C.A','Dulce y amargo.',30,1); -- Modificando_Id_1
call sp_ActualizarPlatos(1,'CERDO AGRIDULCE','Es de color naranja-rojo brillante y su sabor es entre dulce y amargo.',30,1); -- Restaurando_Id_1
-- --------------------------------------------------------------------------------------------------------------- --
-- 9) Servicios_has_Platos
-- --------------------------------------------------------------------------------------------------------------- --
-- --------------------------------------------------------------------------------------------------------------- --
-- 10) Productos
-- --------------------------------------------------------------------------------------------------------------- --
delimiter $$
create procedure sp_ActualizarProductos(in nP varchar(150),in c int,in CPDLP int)
begin
	update Productos set nombreProducto=nP,cantidad=c where codigoProducto=CPDLP;
end $$
delimiter ;
call sp_ActualizarProductos('Gramos de carne de cerdo',5,1); -- Modificando_Id_1
call sp_ActualizarProductos('Gramos de carne de cerdo',500,1); -- Restaurando_Id_1
-- --------------------------------------------------------------------------------------------------------------- --
-- 11) Productos_has_Platos
-- --------------------------------------------------------------------------------------------------------------- --
-- --------------------------------------------------------------------------------------------------------------- --
# ================================================================================================================= #
# ------------------------------------------------sp_Eliminar ----------------------------------------------------- #
# ================================================================================================================= #
-- --------------------------------------------------------------------------------------------------------------- --
-- 1) TipoEmpleado
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_EliminarTipoEmpleado(in cTe int)
begin
start transaction;
	delete from Empleados where codigoTipoEmpleado=cTe;
	delete from TipoEmpleado where codigoTipoEmpleado=cTe;
commit;
end $$
Delimiter ;
call sp_EliminarTipoEmpleado (1000);
-- --------------------------------------------------------------------------------------------------------------- --
-- 2) Empleados
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_EliminarEmpleados(in ce int)
begin
start transaction;
	delete from  Servicios_has_empleados where codigoEmpleado=ce;
	delete from Empleados where codigoEmpleado=ce;
commit;
end $$
Delimiter ;
call sp_EliminarEmpleados (1000);
-- --------------------------------------------------------------------------------------------------------------- --
-- 3) Empresas
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_EliminarEmpresas(in ce int)
begin
start transaction;
	delete from  presupuesto where codigoEmpresa=ce;
	delete from  servicios where codigoEmpresa=ce;
	delete from  empresas where codigoEmpresa=ce;
commit;
end $$
Delimiter ;
call sp_EliminarEmpresas(1000);
-- --------------------------------------------------------------------------------------------------------------- --
-- 4) Servicios
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_EliminarServicios(in cS int)
begin
start transaction;
	delete from servicios_has_empleados where codigoServicio=cS;
	delete from servicios_has_platos where Servicios_codigoServicio=cS;
	delete from servicios where codigoServicio=cS;
commit;
end $$
Delimiter ;
call sp_EliminarServicios(1000);
-- --------------------------------------------------------------------------------------------------------------- --
-- 5) Servicios_has_Empleados
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_EliminarServicios_has_Empleados(in cSaE int)
begin
start transaction;
	delete from Servicios_has_Empleados where codigoServicio=cSaE;
commit;
end $$
Delimiter ;
call sp_EliminarServicios_has_Empleados (1000);
-- --------------------------------------------------------------------------------------------------------------- --
-- 6) Presupuesto
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_EliminarPresupuesto(in cP int)
begin
start transaction;
	delete from Presupuesto where codigoPresupuesto=cP;
commit;
end $$
Delimiter ;
call sp_EliminarPresupuesto (1000);
-- --------------------------------------------------------------------------------------------------------------- --
-- 7) TipoPlato
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_EliminarTipoPlato(in cP int)
begin
start transaction;
	delete from Platos where codigoTipoPlato=cP;
	delete from TipoPlato where codigoTipoPlato=cP;
commit;
end $$
Delimiter ;
call sp_EliminarTipoPlato (1000);
-- --------------------------------------------------------------------------------------------------------------- --
-- 8) Platos
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_EliminarPlatos(in cP int)
begin
start transaction;
	delete from  servicios_has_platos where Platos_codigoPlato=cP;
	delete from  productos_has_platos where Platos_codigoPlato=cP;
	delete from  platos where codigoPlato=cP;
commit;
end $$
Delimiter ;
call sp_EliminarPlatos (1000);
-- --------------------------------------------------------------------------------------------------------------- --
-- 9) Servicios_has_Platos
-- --------------------------------------------------------------------------------------------------------------- --
-- --------------------------------------------------------------------------------------------------------------- --
-- 10) Productos
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_EliminarProductos(in cPrdCtO int)
begin
start transaction;
	delete from productos_has_platos where Productos_codigoProducto=cPrdCtO;
	delete from productos where codigoProducto=cPrdCtO;
commit;
end $$
Delimiter ;
call sp_EliminarProductos (1000);
-- --------------------------------------------------------------------------------------------------------------- --
-- 11) Productos_has_Platos
-- --------------------------------------------------------------------------------------------------------------- --
-- --------------------------------------------------------------------------------------------------------------- --
# ================================================================================================================= #
# -------------------------------------------------sp_Buscar------------------------------------------------------- #
# ================================================================================================================= #
-- --------------------------------------------------------------------------------------------------------------- --
-- 1) TipoEmpleado
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_BuscarTipoEmpleado(in cTe int)
begin
start transaction;
	Select * from TipoEmpleado where codigoTipoEmpleado=cTe;
commit;
end $$
Delimiter ;
call sp_BuscarTipoEmpleado (1);
-- --------------------------------------------------------------------------------------------------------------- --
-- 2) Empleados
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_BuscarEmpleados(in ce int)
begin
start transaction;
	Select * from Empleados where codigoEmpleado=ce;
commit;
end $$
Delimiter ;
call sp_BuscarEmpleados (1);
-- --------------------------------------------------------------------------------------------------------------- --
-- 3) Empresas
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_BuscarEmpresas(in codEmpresa int)
begin
start transaction;
	Select * from  Empresas where codigoEmpresa=codEmpresa;
commit;
end $$
Delimiter ;
call sp_BuscarEmpresas(1);
-- --------------------------------------------------------------------------------------------------------------- --
-- 4) Servicios
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_BuscarServicios(in cS int)
begin
start transaction;
	Select * from servicios where codigoServicio=cS;
commit;
end $$
Delimiter ;
call sp_BuscarServicios(1);
-- --------------------------------------------------------------------------------------------------------------- --
-- 5) Servicios_has_Empleados
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_BuscarServicios_has_Empleados(in cSaE int,in cEcE int)
begin
start transaction;
	Select * from Servicios_has_Empleados where codigoServicio=cSaE and codigoEmpleado=cEcE;
commit;
end $$
Delimiter ;
call sp_BuscarServicios_has_Empleados (1,1);
-- --------------------------------------------------------------------------------------------------------------- --
-- 6) Presupuesto
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_BuscarPresupuesto(in cP int)
begin
start transaction;
	Select * from Presupuesto where codigoPresupuesto=cP;
commit;
end $$
Delimiter ;
call sp_BuscarPresupuesto(1);
-- --------------------------------------------------------------------------------------------------------------- --
-- 7) TipoPlato
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_BuscarTipoPlato(in cP int)
begin
start transaction;
	Select * from TipoPlato where codigoTipoPlato=cP;
commit;
end $$
Delimiter ;
call sp_BuscarTipoPlato(1);
-- --------------------------------------------------------------------------------------------------------------- --
-- 8) Platos
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_BuscarPlatos(in cP int)
begin
start transaction;
	Select * from  platos where codigoPlato=cP;
commit;
end $$
Delimiter ;
call sp_BuscarPlatos(1);
-- --------------------------------------------------------------------------------------------------------------- --
-- 9) Servicios_has_Platos
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_BuscarServicios_has_Platos(in cSaP int)
begin
start transaction;
	Select * from Servicios_has_Platos where Servicios_codigoServicio=cSaP;
commit;
end $$
Delimiter ;
call sp_BuscarServicios_has_Platos(1);
-- --------------------------------------------------------------------------------------------------------------- --
-- 10) Productos
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_BuscarProductos(in cPrdCtO int)
begin
start transaction;
	Select * from productos where codigoProducto=cPrdCtO;
commit;
end $$
Delimiter ;
call sp_BuscarProductos(1);
-- --------------------------------------------------------------------------------------------------------------- --
-- 11) Productos_has_Platos
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_BuscarProductos_has_Platos(in cPaPL int)
begin
start transaction;
	Select * from Productos_has_Platos where Productos_codigoProducto=cPaPL;
commit;
end $$
Delimiter ;
call sp_BuscarProductos_has_Platos(1);
-- --------------------------------------------------------------------------------------------------------------- --
# ================================================================================================================= #
# -------------------------------------------------sp_Listar------------------------------------------------------- #
# ================================================================================================================= #
-- --------------------------------------------------------------------------------------------------------------- --
-- 1) TipoEmpleado
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_ListarTipoEmpleado()
begin
start transaction;
	Select 
		codigoTipoEmpleado,
        descripcion 
	from 
		TipoEmpleado;
commit;
end $$
Delimiter ;
call sp_ListarTipoEmpleado();
-- --------------------------------------------------------------------------------------------------------------- --
-- 2) Empleados
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_ListarEmpleados()
begin
start transaction;
	Select
		codigoEmpleado,
        numeroEmpleado,
        apellidosEmpleado,
		nombresEmpleado,
        direccionEmpleado,
        telefonoContacto,
		gradoCocinero,
        codigoTipoEmpleado
    from
		Empleados;
commit;
end $$
Delimiter ;
call sp_ListarEmpleados();
-- --------------------------------------------------------------------------------------------------------------- --
-- 3) Empresas
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_ListarEmpresas()
begin
start transaction;
	Select
		codigoEmpresa,
		nombreEmpresa,
		direccion,
		telefono
    from
		Empresas;
commit;
end $$
Delimiter ;
call sp_ListarEmpresas();
-- --------------------------------------------------------------------------------------------------------------- --
-- 4) Servicios
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_ListarServicios()
begin
start transaction;
	Select
		codigoServicio,
		fechaServicio,
		tipoServicio,
		horaServicio,
		lugarServicio,
		telefonoContacto,
		codigoEmpresa
    from
		Servicios;
commit;
end $$
Delimiter ;
call sp_ListarServicios();
-- --------------------------------------------------------------------------------------------------------------- --
-- 5) Servicios_has_Empleados
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_ListarServicios_has_Empleados()
begin
start transaction;
	Select
		codigoServicio,
		codigoEmpleado,
		fechaEvento,
		horaEvento,
		lugarEvento
    from
		Servicios_has_Empleados;
commit;
end $$
Delimiter ;
call sp_ListarServicios_has_Empleados();
-- --------------------------------------------------------------------------------------------------------------- --
-- 6) Presupuesto
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_ListarPresupuesto()
begin
start transaction;
	Select
		codigoPresupuesto,
		fechaSolicitud, 
		cantidadPresupuesto, 
		codigoEmpresa
    from
		Presupuesto;
commit;
end $$
Delimiter ;
call sp_ListarPresupuesto();
-- --------------------------------------------------------------------------------------------------------------- --
-- 7) TipoPlato
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_ListarTipoPlato()
begin
start transaction;
	Select
		codigoTipoPlato,
		descripcionTipo
    from
		TipoPlato;
commit;
end $$
Delimiter ;
call sp_ListarTipoPlato();
-- --------------------------------------------------------------------------------------------------------------- --
-- 8) Platos
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_ListarPlatos()
begin
start transaction;
	Select
		codigoPlato,
		cantidad, 
		nombrePlato, 
		descripcionPlato, 
		precioPlato, 
		codigoTipoPlato
    from
		Platos;
commit;
end $$
Delimiter ;
call sp_ListarPlatos();
-- --------------------------------------------------------------------------------------------------------------- --
-- 9) Servicios_has_Platos
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_ListarServicios_has_Platos()
begin
start transaction;
	Select
		S.codigoServicio,P.codigoPlato  
    from
		Servicios S,Platos P
    order by S.codigoServicio asc,P.codigoPlato asc ;
commit;
end $$
Delimiter ;
call sp_ListarServicios_has_Platos();

-- --------------------------------------------------------------------------------------------------------------- --
-- 10) Productos
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_ListarProductos()
begin
start transaction;
	Select
		codigoProducto,
		nombreProducto,
		cantidad
    from
		Productos;
commit;
end $$
Delimiter ;
call sp_ListarProductos();
-- --------------------------------------------------------------------------------------------------------------- --
-- 11) Productos_has_Platos
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
create procedure sp_ListarProductos_has_Platos()
begin
start transaction;
	Select
		PR.codigoProducto,PL.codigoPlato
    from
		Productos PR,Platos PL
     order by PR.codigoProducto asc,PL.codigoPlato asc;
commit;
end $$
Delimiter ;
call sp_ListarProductos_has_Platos();
-- --------------------------------------------------------------------------------------------------------------- --
# ================================================================================================================= #
# -------------------------------------------------REPORTES-------------------------------------------------------- #
# ================================================================================================================= #
-- --------------------------------------------------------------------------------------------------------------- --
-- sp_ListarReporte --ANTES DE LA MODIFICACION 
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
Create procedure sp_ListarReporte(in codEmpresa int)
begin
	Select * from Empresas E inner join Presupuesto P on
    E.codigoEmpresa = P.codigoEmpresa
	inner join Servicios S on
		P.codigoEmpresa = S.codigoEmpresa
        where E.codigoEmpresa = codEmpresa  group by S.tipoServicio HAVING COUNT(*)>1 ;
end $$
Delimiter ;
call sp_ListarReporte(2);
-- --------------------------------------------------------------------------------------------------------------- --
-- sp_ListarReporteDos
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
Create procedure sp_ListarReporteDos(in codServicio int)
begin
	Select * from Servicios S inner join Platos P
	inner join TipoPlato Tp on
		P.codigoTipoPlato = Tp.codigoTipoPlato 
        where S.codigoServicio = codServicio order by P.codigoPlato asc 
;
end $$
Delimiter ;
call sp_ListarReporteDos(1);
# ================================================================================================================= #
# -------------------------------------------------Reporte Presupuesto--------------------------------------------- #
# ================================================================================================================= #
-- --------------------------------------------------------------------------------------------------------------- --
-- sp_ListarSubReportePresupuestoFinal
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
Create procedure sp_ListarSubReportePresupuestoFinal(in codEmpresa int)
begin
	select * from Empresas E inner join Presupuesto P on	
	E.codigoEmpresa=P.codigoEmpresa where E.codigoEmpresa=codEmpresa
	group by P.cantidadPresupuesto
;
end $$
Delimiter ;
call sp_ListarSubReportePresupuestoFinal(1);
-- --------------------------------------------------------------------------------------------------------------- --
-- sp_ListarReportePresupuestoFinal
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
Create procedure sp_ListarReportePresupuestoFinal(in codEmpresa int)
begin
	Select * from Empresas E inner join Servicios S on
	E.codigoEmpresa = S.codigoEmpresa where E.codigoEmpresa=codEmpresa
;
end $$
Delimiter ;
call sp_ListarReportePresupuestoFinal(1);
-- --------------------------------------------------------------------------------------------------------------- --
# ================================================================================================================= #
# -------------------------------------------------Reporte Servicio ----------------------------------------------- #
# ================================================================================================================= #
-- --------------------------------------------------------------------------------------------------------------- --
-- sp_ListarReporteServiciosFinal
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
Create procedure sp_ListarReporteServiciosFinal(in codServicio int)
begin
	Select * from Servicios S where S.codigoServicio = codServicio;
end $$
Delimiter ;
call sp_ListarReporteServiciosFinal(1);
-- --------------------------------------------------------------------------------------------------------------- --
-- sp_ListarReporteServiciosFinalSubreportePlatos
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
Create procedure sp_ListarRPServiciosDePlatosTipoPlato()
begin
	Select * from Platos P inner join TipoPlato TP on
    P.codigoTipoPlato = TP.codigoTipoPlato;
end $$
Delimiter ;
call sp_ListarRPServiciosDePlatosTipoPlato();
-- --------------------------------------------------------------------------------------------------------------- --
-- sp_ListarSubreportedeProductosparaServicios
-- --------------------------------------------------------------------------------------------------------------- --
Delimiter $$
Create procedure sp_ListarSubreportedeProductosparaServicios(in codServicio int)
begin
	select Pr.nombreProducto,Pr.cantidad from Productos Pr, Servicios S where S.codigoServicio = codServicio;
end $$
Delimiter ;
call sp_ListarSubreportedeProductosparaServicios(1);
# ================================================================================================================= #
