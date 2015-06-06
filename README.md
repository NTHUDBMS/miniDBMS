# miniDBMS -- Team 6
CS4710 Introducing to Databases

GIT repository: https://github.com/NTHUDBMS/miniDBMS.git

Member: 
-----------------------------------------------------------
	Paul, Shawn(蔡宇翔), Mike(張書正)

Description:
-----------------------------------------------------------
	This Database Manage System simulator is SQL3 
	required, other version of SQL commands are 
	not under this project specification.

	We use ANTLR 4 tool to implement interpreter.

	The System will input .sql file as arguments
	to execute the simulation

Execute Steps:
-----------------------------------------------------------
	1.Please import into eclipse as a java project 

	2.Must install "ANTLR 4 IDE" from "Eclipse Marketplace" 
	(Eclipse->Help->Eclipse Marketplace->search"ANTLR"->install)

	3.Add External JARS:
	(Project->Properties->Java Build Path->Add External JARS)
	(project_dir)\JavaLib\antlr-4.4-complete.jar
	(project_dir)\JavaLib\google-collect-1.0.jar

	4.Execute the project with test benches of SQL files as argument
	(Eclipse->Run->Run Configurations->Arguments)
	Arguments should be ordered as below:
	(project_dir)\Testbench\DBDemo3\tables.sql
	(project_dir)\Testbench\DBDemo3\abstracts.sql
	(project_dir)\Testbench\DBDemo3\stopwords.sql
	(project_dir)\Testbench\DBDemo3\testDemo3.sql

	5.Main is in "dbms.DBMS.java", open it and execute
	
	6.The result should be displace on Console
	
	P.S.
	After execution, it will generate ".db" ".dat" file by 
	CREATE INSERT commands. 
	If you want to run again, delete them

