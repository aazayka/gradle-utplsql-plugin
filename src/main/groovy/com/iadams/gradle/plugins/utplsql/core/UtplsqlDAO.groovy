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
        def result = sql.rows("select status from user_objects where object_name = '${packageName.toString().toUpperCase()}'")

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
    int runUtplsqlProcedure(String packageName, String testMethod, boolean setupMethod) throws SQLException
    {
        def setup = testMethod.equals('test') ? ' recompile_in => FALSE,' : ''

        sql.call("{call utplsql.${Sql.expand(testMethod)}('${Sql.expand(packageName)}', ${Sql.expand(setup)} per_method_setup_in => ${Sql.expand(setupMethod.toString())}); $Sql.VARCHAR := utplsql2.runnum}") { result ->
            return result
        }
    }
}
