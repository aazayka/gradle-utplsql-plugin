package com.iadams.gradle.plugins.utplsql.tasks

import groovy.sql.Sql
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import java.sql.SQLException

/**
 * Created by Iain Adams on 13/09/2014.
 */
class DeployTestsTask extends DefaultTask {
    /**
     * The JDBC driver to use. Defaults to Oracle.
     *
     */
    @Input
    String driver
    /**
     * The JDBC URL to use.
     *
     */
    @Input
    String url

    /**
     * The username to connect to the database.
     *
     */
    @Input
    String username

    /**
     * The password to connect to the database.
     *
     */
    @Input
    String password

    /**
     * Location to load all the tests from.
     *
     */
    @Input
    File inputDirectory

    /**
     * If there is a connection problem or DB PLSQL installation error the plugin reports 0 tests run. This is an error condition which should be
     * flagged, by default an error exception shall be raised if 0 tests are run. This state can be unset in the plugin configuration if desired
     *
     */
    @Input
    @Optional
    Boolean failOnNoTests

    /**
     * If errors occur the failure message can be written to the console.
     *
     */
    @Input
    @Optional
    Boolean outputFailuresToConsole

    @TaskAction
    void deployUtplsqlTests() {

        logger.quiet "Deploying ${inputDirectory.listFiles().size()} Unit Test Files"

        logger.info "URL: ${getUrl()}"
        logger.info "Username: ${getUsername()}"
        logger.info "Driver: ${getDriver()}"

        try {
            def sql = Sql.newInstance(getUrl() ,getUsername() ,getPassword() ,getDriver())

            inputDirectory.eachFileRecurse{
                if(it.name.endsWith('.pks')) {
                    logger.info "Deploying: $it.name"
                    sql.execute( it.text[0..it.text.lastIndexOf(';')] )
                }
                if(it.name.endsWith('.pkb')) {
                    logger.info "Deploying: $it.name"
                    sql.execute( it.text[0..it.text.lastIndexOf(';')] )
                }
            }
        }
        catch (ClassNotFoundException e) {
            throw new GradleException("JDBC Driver class not found", e);
        }
        catch (SQLException e) {
            throw new GradleException("Error communicating with the database", e)
        }
    }
}
