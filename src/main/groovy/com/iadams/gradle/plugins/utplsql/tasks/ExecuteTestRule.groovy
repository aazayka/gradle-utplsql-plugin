package com.iadams.gradle.plugins.utplsql.tasks

import org.gradle.api.Rule
import org.gradle.api.Project

/**
 * Created by Iain Adams on 18/10/2014.
 */
class ExecuteTestRule implements Rule {

    static final String PREFIX = "executeTest"
    Project project

    ExecuteTestRule(Project project) {
        this.project = project
    }

    String getDescription() {
        String.format("Pattern: %s<TestName>: Executes a specific UTPLSQL test.", PREFIX)
    }

    @Override
    String toString() {
        String.format("Rule: %s", getDescription())
    }

    void apply(String taskName) {
        if (taskName.startsWith(PREFIX)) {
            project.getTasks().create(taskName)

            //TODO complete the rule to execute a specific test.
        }
    }
}
