package com.iadams.gradle.plugins.utplsql.extensions

import org.gradle.api.Project
/**
 * Created by Iain Adams.
 */
class UtplsqlPluginExtension {
    /**
     * The JDBC driver to use. Defaults to Oracle.
     */
    String driver = 'oracle.jdbc.driver.OracleDriver'

    /**
     * The JDBC URL to use.
     */
    String url = ''

    /**
     * The username to connect to the database.
     */
    String username = ''

    /**
     * The password to connect to the database.
     */
    String password = ''

    /**
     * The source directory for all the unit tests.
     */
    String sourceDir

    /**
     * Filter for which files to include in the sourceDir
     */
    String includes = '**/*.pks, **/*.pkb'

    /**
     * Filter for which files to excludes in the sourceDir
     */
    String excludes = ''

    /**
     * The output directory for the test results.
     */
    String outputDir

    /**
     * The type of test method to execute. Can be either test or run. Defaults to test.
     */
    String testMethod = 'run'

    /**
     * The setup method to use. set to TRUE to execute setup and teardown for each procedure. FALSE to execute for each package. Default is FALSE.
     */
    Boolean setupMethod = false

    /**
    * In the event of no tests being run, fail the build.
    */
    Boolean failOnNoTests = true
    Boolean outputFailuresToConsole

    UtplsqlPluginExtension( Project project) {
        sourceDir = "${project.projectDir}/src/test/plsql"
        outputDir = "${project.buildDir}/utplsql"
    }
}