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

import java.util.regex.Matcher

/**
 * Created by Iain Adams on 23/09/2014.
 */
class ResultParser {
  private static final String ASSERT_TYPE_UNKNOWN = 'Assert Type Unknown'

  /**
   * Parse the given description breaking down into its individual parts.
   *
   * @param description
   * @return
   * @throws ResultParserException
   */
  static TestDescription ParseDescription(def description) throws ResultParserException {

    TestDescription result = new TestDescription()
    def descComponents = description.split(':')
    def nameComponents = descComponents[0].split('\\.')

    switch (nameComponents.length) {
      case 2:
        result.procedureName = nameComponents[0] + '.' + nameComponents[1]

        def postAssertStr2 = extractAssertType(result, descComponents[1])

        def postTestDesc2 = extractTestName(result, postAssertStr2)

        result.results = getResults(postTestDesc2, descComponents, 2)

        break
      default:
        throw new ResultParserException("Unable to parse utPLSQL results, check the outcome table.")
    }
    result
  }

  /**
   *
   * @param result
   * @param typeStr
   * @return
   */
  static def extractAssertType(TestDescription result, String typeStr) {

    def quotePos = typeStr.indexOf('"')
    if (quotePos > 0) {
      result.type = typeStr[0..quotePos - 1].trim()
      typeStr[quotePos + 1..-1]
    } else {
      result.type = ASSERT_TYPE_UNKNOWN
      typeStr
    }
  }

  /**
   *
   * @param result
   * @param typeStr
   * @return
   */
  static def extractTestName(TestDescription result, String testStr) {
    switch (testStr) {
      case ~/(.*)" Result(.*)/:
        result.testName = Matcher.lastMatcher[0][1]
        break
      case ~/(.*)" Expected(.*)/:
        result.testName = Matcher.lastMatcher[0][1]
        return testStr - Matcher.lastMatcher[0][1] - '" '
      default:
        result.testName = testStr.trim()
    }
    null
  }

  /**
   *
   * @param testStr
   * @param desc
   * @param startIndex
   * @return
   */
  static String getResults(String testStr, String[] desc, int startIndex) {
    //if the test string is null fetch the description
    testStr = testStr ?: fetchDesc(desc, startIndex)
  }

  /**
   *
   * @param desc
   * @param startIndex
   * @return
   */
  static String fetchDesc(String[] desc, int startIndex) {
    def description = ''
    (startIndex..desc.size() - 1).each {
      description += desc[it]
    }
    description.trim()
  }
}
