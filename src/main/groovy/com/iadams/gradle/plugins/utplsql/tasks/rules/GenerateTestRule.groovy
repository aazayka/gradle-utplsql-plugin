package com.iadams.gradle.plugins.utplsql.tasks.rules

import com.iadams.gradle.plugins.utplsql.tasks.GenerateTestTask
import com.iadams.gradle.plugins.utplsql.tasks.RunTestsTask
import org.gradle.api.Project
import org.gradle.api.Rule

/**
 * Created by Iain Adams on 18/10/2014.
 */
class GenerateTestRule implements Rule {

    static final String PREFIX = "utGen-"
    Project project

    GenerateTestRule(Project project) {
        this.project = project
    }

    String getDescription() {
        String.format("Pattern: %s<TestName>: Generates UTPLSQL test spec and body shell.", PREFIX)
    }

    @Override
    String toString() {
        String.format("Rule: %s", getDescription())
    }

    void apply(String taskName) {
        if (taskName.startsWith(PREFIX)) {
            project.task(taskName, type: GenerateTestTask) {

                def extension = project.extensions.utplsql

                testName = taskName - PREFIX
                sourceDir = extension.sourceDir
            }
        }
    }
}
