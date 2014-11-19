package com.iadams.gradle.plugins.utplsql.extensions

/**
 * Created by Iain Adams.
 */
class UtplsqlPluginExtension {
    //TODO complete a full range of extensions allowing configuration of output / source dirs etc.
    /**
     * The JDBC driver to use. Defaults to Oracle.
     */
    String driver = 'oracle.jdbc.driver.OracleDriver'

    /**
     * The JDBC URL to use.
     */
    String url

    /**
     * The username to connect to the database.
     */
    String username

    /**
     * The password to connect to the database.
     */
    String password

    /**
     * The source directory for all the unit tests.
     */
    String sourceDir = 'src/test/plsql'

    /**
     * The output directory for the test results.
     */
    String outputDir = 'build/utplsql'

    /**
     * The type of test method to execute. Can be either test or run. Defaults to test.
     */
    String testMethod = 'test'

    def packages = []

    /**
     * The setup method to use. set to TRUE to execute setup and teardown for each procedure. FALSE to execute for each package. Default is FALSE.
     */
    Boolean setupMethod = false

    /**
    * In the event of no tests being run, fail the build.
    */
    Boolean failOnNoTests
    Boolean outputFailuresToConsole
}