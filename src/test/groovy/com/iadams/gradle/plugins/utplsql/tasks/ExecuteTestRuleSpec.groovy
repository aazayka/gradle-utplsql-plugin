package com.iadams.gradle.plugins.utplsql.tasks

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * Created by Iain Adams
 */
class ExecuteTestRuleSpec extends Specification {
    Project project

    void setup(){
        project = ProjectBuilder.builder().build()
    }

    def "add a task from the rule"() {
        expect:
            project.tasks.findByName( 'executeTestBetwnstr' ) == null

        when:
            project.task( 'executeTestBetwnstr' )

        then:
            Task task = project.tasks.findByName( 'executeTestBetwnstr' )
            task != null
    }
}
