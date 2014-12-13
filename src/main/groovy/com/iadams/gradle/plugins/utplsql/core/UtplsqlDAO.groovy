package com.iadams.gradle.plugins.utplsql.core

import groovy.sql.Sql

import java.sql.SQLException

/**
 * Created by iwarapter.
 */
class UtplsqlDAO {

    Sql sql

    UtplsqlDAO(Sql conn){
        this.sql = conn
    }


    //TODO add enum for package status responses
    //TODO define a DAO custom exception

    /**
     * @param packageName
     *
     * @return
     * @throws SQLException
     */
    def getPackageStatus(String packageName) throws SQLException
    {
        def result = sql.rows("select status from user_objects where object_name = ${packageName.toString().toUpperCase()}")

        if(result.size() == 0){
            return 'NO PACKAGE FOUND'
        }

        if(result[0].STATUS == 'VALID') {
            return 'VALID'
        }
        else {
            return 'INVALID'
        }
    }

    /**
     * @param packageName
     * @param testMethod
     * @param setupMethod
     *
     * @return
     * @throws SQLException
     */
    def runUtplsqlProcedure(String packageName, String testMethod, boolean setupMethod) throws SQLException
    {
        def setup = testMethod.equals('test') ? ' recompile_in => FALSE,' : ''

        def returnVal

        sql.call("{call utplsql.${Sql.expand(testMethod)}('${Sql.expand(packageName)}', ${Sql.expand(setup)} per_method_setup_in => ${Sql.expand(setupMethod.toString())}); $Sql.VARCHAR := utplsql2.runnum}") { result ->
            returnVal = result            
        }

        return returnVal
    }

    /**
     * @param packageName
     *
     * @return
     * @throws SQLException
     */
    def recompilePackage(String packageName) throws UtplsqlDAOException
    {
        try {
            sql.execute("ALTER PACKAGE ${Sql.expand(packageName.toString().toUpperCase())} COMPILE")
            sql.execute("ALTER PACKAGE ${Sql.expand(packageName.toString().toUpperCase())} COMPILE BODY")

            if(getPackageStatus(packageName)!='VALID'){
                throw new UtplsqlDAOException("The package $packageName failed to compile.")
            }
        }
        catch(SQLException e){
            throw new UtplsqlDAOException("Error communicating with the database.", e)
        }
    }
}
