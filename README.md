# Сервис по полученю актов сверки по запросу пользователя

## Требования к ПО
### ОС: Windows-based, Linux-based
### Server: Tomcat9

## Требования к серверу БД
### Прилинкованный сервер LDAP (ADSI)

## Структура БД
### Таблица Users
#### Столбцы: 
	id [int] IDENTITY(1,1) NOT NULL
	login [nvarchar](64) NULL
	pass [nvarchar](512) NULL
	role [nvarchar](64) NULL
	email [nvarchar](128) NULL
	isActive [bit] NULL
	lastLoginedDate [datetime] NULL
	joinedDate [datetime] NULL
	lastModUser [varchar](64) NULL
	
### Таблица Files
#### Столбцы:
	fName [nvarchar](255) NULL
	content [varbinary](max) NULL
	changedate [datetime] getdate()
	idCA [int] NULL
	be [nvarchar](10) NULL
	dateFrom [nvarchar](6) NULL
	dateTo [nvarchar](6) NULL
	
### Таблица BE
#### Столбцы:
	ID_BE [int] NULL
	BE [nvarchar](10) NULL