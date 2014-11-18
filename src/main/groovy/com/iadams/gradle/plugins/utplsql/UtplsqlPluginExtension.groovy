package com.iadams.gradle.plugins.utplsql

/**
 * Created by Iain Adams on 04/10/2014.
 */
class UtplsqlPluginExtension {
    //TODO complete a full range of extensions allowing configuration of output / source dirs etc.
    String driver = "oracle.jdbc.driver.OracleDriver"
    String url
    String username
    String password
    String outputDir = "build/utplsql"
    String testMethod
    def packages = []
    String setupMethod
    Boolean failOnNoTests
    Boolean outputFailuresToConsole
}