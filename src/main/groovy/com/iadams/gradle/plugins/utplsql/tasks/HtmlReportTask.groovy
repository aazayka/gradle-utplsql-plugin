package com.iadams.gradle.plugins.utplsql.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Created by iwarapter on 13/12/14.
 */
class HtmlReportTask extends DefaultTask {

    /**
     * Location to which we will find the xml report files.
     *
     */
    @OutputDirectory
    File resultsDir

    /**
     * Location to which we will write the report file. Defaults to the gradle buildDir.
     *
     */
    @OutputDirectory
    File reportsDir

    @TaskAction
    void runTask(){

        ClassLoader antClassLoader = org.apache.tools.ant.Project.class.classLoader
        project.buildscript.configurations.classpath.each { File f ->
          antClassLoader.addURL(f.toURI().toURL())
        }

        ant.taskdef(
            name: 'junitreport',
            classname: 'org.apache.tools.ant.taskdefs.optional.junit.XMLResultAggregator',
            classpath: project.configurations.junitreport.asPath
        )

        ant.junitreport(todir: "$reportsDir") {
            fileset(dir: "$resultsDir", includes: 'TEST-*.xml')
            report(todir: "$reportsDir", format: "frames")
        }
    }
}
