package com.iadams.gradle.plugins.utplsql

/**
 * Created by Iain Adams on 04/10/2014.
 */
class UtplsqlPluginExtension {
    String driver = "oracle.jdbc.driver.OracleDriver"
    String url
    String username
    String password
    String testMethod
    def packages = []
    String setupMethod
    Boolean failOnNoTests
    Boolean outputFailuresToConsole
}