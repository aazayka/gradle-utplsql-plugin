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
  void runTask() {

    ClassLoader antClassLoader = org.apache.tools.ant.Project.class.classLoader
    project.buildscript.configurations.classpath.each {File f ->
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
