package com.iadams.gradle.plugins.utplsql.tasks

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * Created by Iain Adams
 */
class RunTestsTaskSpec extends Specification {
    static final TASK_NAME = 'runTests'
    Project project

    void setup(){
        project = ProjectBuilder.builder().build()
    }

    def "create task with appropriate configuration"(){
        expect:
        project.tasks.findByName( TASK_NAME ) == null

        when:
        project.task( TASK_NAME, type: RunTestsTask )

        then:
        Task task = project.tasks.findByName( TASK_NAME )
        task != null
    }
}
