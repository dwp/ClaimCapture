Setup Liquibase
===============

1) Installation
use brew:  brew install liquibase
or download jar and add it to your path 

2) Validate your installation by running:
  liquibase --url="jdbc:postgresql://localhost:5432/carerstransactions_db" --classpath=<location driver>/postgresql.jar validate 

3) If running validate fails try running with debug:
  liquibase --url="jdbc:postgresql://localhost:5432/carerstransactions_db" --classpath=<location driver>/postgresql.jar --logLevel=DEBUG validate


Creating the SCHEMA
===================

This will create STP_ALPHA_1 role, the STP and STP_SECURE schemas and the tables for those schemas.

  liquibase --url="jdbc:postgresql://localhost:5432/carerstransactions_db" --classpath=<location driver>/postgresql.jar update


Dropping everything
======================

From the command line
  liquibase --url="jdbc:postgresql://localhost:5432/carerstransactions_db" --classpath=<location driver>/postgresql.jar dropAll
