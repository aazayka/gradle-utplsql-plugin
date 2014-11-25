package com.iadams.gradle.plugins.utplsql.tasks

import com.iadams.gradle.plugins.utplsql.UtplsqlPlugin
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
    File sourceDir

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

        def extension = project.extensions.findByName(UtplsqlPlugin.UTPLSQL_EXTENSION)

        def packages = new FileNameFinder().getFileNames(sourceDir.absolutePath, extension.includes, extension.excludes)
        packages = packages.collect { project.file(it) }
        packages = packages.unique { a, b -> a <=> b }

        logger.info "Deploying ${sourceDir.listFiles().size()} Unit Test Files"
        logger.info "URL: ${getUrl()}"
        logger.info "Username: ${getUsername()}"
        logger.info "Driver: ${getDriver()}"
        logger.info "Packages: ${packages}"

        try {
            project.configurations.driver.each {File file ->
                project.gradle.class.classLoader.addURL(file.toURL())
            }

            def sql = Sql.newInstance(getUrl() ,getUsername() ,getPassword() ,getDriver())

            packages.each{
                logger.info "Deploying: $it.name"
                sql.execute( it.text[0..it.text.lastIndexOf(';')] )
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
