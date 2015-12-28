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
package com.iadams.gradle.plugins.utplsql.core

import groovy.xml.MarkupBuilder

/**
 * Created by Iain Adams on 10/09/2014.
 */
class PackageTestResults {

  def descriptions = []

  def getTestsRun() {
    descriptions.size()
  }

  def getTestFailures() {
    descriptions.count {it.failure}
  }

  def getTestErrors() {
    descriptions.count {it.error}
  }

  String toXML(String name, def duration) {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.setDoubleQuotes(true)

    xml.testsuite(name: name, tests: getTestsRun(), failures: getTestFailures(), skipped: '0', errors: getTestErrors(), time: duration) {
      descriptions.each {it.toXML(xml)}
    }

    writer.toString()
  }
}
