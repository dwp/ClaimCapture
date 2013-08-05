#!/bin/ksh

# author: Jorge Migueis
# description: 
#   Test of the get_new_transaction postgres function

#---------------------------------------
# Context & Variables
#---------------------------------------

# Defines global variables
set_global_variables() {  
  # Save environment variables before we modify them
  # We shall reapply them at the end of the script
  _OLDSPS4=${PS4}
  _PROCESS=$$ 
  # Info
  _VERSION='1.2' 
  _LASTUPDATE='30/07/2013'  
  _NAMESCRIPT=$0 
  
  # Misc
  _ERROR=0
}  

#---------------------------------------
# Error messages
#--------------------------------------- 
function error_usage {
  print "ERROR: $1"
  print "Usage: " 
  arguments
  _ERROR=2
  stop_batch  
} 


function error_db {  
  print "ERROR: Postgres returned an Error."  
  print "ERROR: $1"
  _ERROR=1
  stop_batch
}
 
#---------------------------------------
# Usage  & Help
#---------------------------------------  

# Display version of this script
function version {
  print "${_NAMESCRIPT} version ${_VERSION} - issued ${_LASTUPDATE}."
}

function arguments {
  print "  ${_NAMESCRIPT} [-hv] [-sd <databse name>]"
}
                                    
function usage {
  print "\nVersion"
  version
  print "\nUsage"
  arguments
  print "\nDescription"
  print "  This script connects to specified database, creates carers_c3 user," 
  print "  creates the transacionids table,installs the function get_new_transaction_id()"
  print "  and runs a test to check installation."
  print "  "
  print "\nOptions"
  print "  -d <database name>  Name of the database where the new table and function must be created."            
  print "  -h                  Display this message and exit."
  print "  -s                  Installs only the PosgreSQL function. Do not create table and user."
  print "  -v                  Display version information."
  print "\nExamples"
  print "  ${_NAMESCRIPT} -v"
  print "  Display the version number of the script.\n"
  print "  ${_NAMESCRIPT} -d carerstransactions_db"
  print "  Connects to database carerstransactions_db, create table, install stored procedure and run smoke test..\n"
  stop_batch                 
}  

function check_parameters {
while getopts "vhsd:" opt; do
  case $opt in
    v)
      version
      stop_batch
      ;;
    h )
      usage
      stop_batch
      ;;
    s) _FUNCTIONONLY=1
      ;;
    d)
     _DATABASENAME=${OPTARG}
     ;;
    esac 
  done
  [[ -z ${_DATABASENAME} ]] && error_usage "Database name must be provided."
  
}
      
#---------------------------------------
# Stop batch
#---------------------------------------
# Trap interruption signals
trap 'stop_batch' 1 2 3 9 15 18

stop_batch() {
  # Set the environment back to its previous state
  PS4=${_OLDSPS4}
  exit ${_ERROR:-0} 
}
  
#---------------------------------------
# Database
#---------------------------------------
  
function run_psql_command {
  echo $1 | psql --set=ON_ERROR_STOP=on -d ${_DATABASENAME}
  (($? != 0)) && error_db "Command failed: $1"  
} 

function run_psql_command_skipping_error {
  echo $1 | psql --set=ON_ERROR_STOP=off -d ${_DATABASENAME}
  (($? != 0)) && error_db "Command failed: $1"  
}

#---------------------------------------
# Main
#---------------------------------------
set_global_variables
check_parameters $@
((  ${_FUNCTIONONLY:-0} == 0 )) &&  run_psql_command '\i create_transactionids_table.sql;'
((  ${_FUNCTIONONLY:-0} == 0 )) &&  run_psql_command '\i create_transactionstatus_table.sql;'
run_psql_command '\i get_transaction.sql;'
run_psql_command '\x \\ select get_new_transaction_id();'
run_psql_command_skipping_error '\x \\ DROP OWNED by carers_c3;'
((  ${_FUNCTIONONLY:-0} == 0 )) &&  run_psql_command '\i create_users.sql;'   
echo 'Installation and smoke test successful'

