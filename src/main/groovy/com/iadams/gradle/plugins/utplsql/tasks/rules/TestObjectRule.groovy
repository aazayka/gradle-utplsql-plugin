/*
 * Gradle Utplsql Plugin
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Iain Adams
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.iadams.gradle.plugins.utplsql.tasks.rules

import com.iadams.gradle.plugins.utplsql.tasks.RunTestsTask
import org.gradle.api.Project
import org.gradle.api.Rule

/**
 * Created by Iain Adams on 18/10/2014.
 */
class TestObjectRule implements Rule {

  static final String PREFIX = "utTest-"
  Project project

  TestObjectRule(Project project) {
    this.project = project
  }

  String getDescription() {
    String.format("Pattern: %s<TestName>: Executes the UTPLSQL tests for a specific object.", PREFIX)
  }

  @Override
  String toString() {
    String.format("Rule: %s", getDescription())
  }

  void apply(String taskName) {
    if (taskName.startsWith(PREFIX)) {
      project.task(taskName, type: RunTestsTask) {

        def extension = project.extensions.utplsql

        def packageName = taskName - PREFIX
        driver = extension.driver
        url = extension.url
        username = extension.username
        password = extension.password
        testMethod = 'test'
        packages = [packageName]
        setupMethod = extension.setupMethod
        outputDir = project.file(extension.outputDir)
        failOnNoTests = extension.failOnNoTests
      }
    }
  }
}
