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

/**
 * Created by Iain Adams on 30/09/2014.
 */
class TestXmlFixtures {
  protected static final String SUCCESSFUL_TEST_DESCRIPTION = """
    <testcase name="null end" classname="BETWNSTR.UT_BETWNSTR" time="0" />
    """

  protected static final String FAILURE_TEST_DESCRIPTION = """
    <testcase name="normal" classname="BETWNSTR.UT_BETWNSTR" time="0">
     <failure type="EQ">Expected "cde" and got "dde"</failure>
    </testcase>
    """

  protected static final String SINGLE_BETWNSTR_XML_RESULTS = """
            <testsuite name="UT_BETWNSTR" tests="1" failures="0" skipped="0" errors="0" time="0.123">
              <testcase name="null end" classname="BETWNSTR.UT_BETWNSTR" time="0" />
            </testsuite>
    """

  protected static final String SINGLE_FAILURE_XML_RESULTS = """
            <testsuite name="UT_BETWNSTR" tests="1" failures="1" skipped="0" errors="0" time="0.123">
              <testcase name="normal" classname="BETWNSTR.UT_BETWNSTR" time="0">
                <failure type="EQ">Expected "cde" and got "dde"</failure>
              </testcase>
            </testsuite>
    """

  protected static final String SINGLE_ERROR_XML_RESULTS = """<testsuite name="UT_CHEESE" tests="1" failures="0" skipped="0" errors="1" time="0.145">
               <testcase name="" classname="" time="0">
                 <error type="INVALID">Warning: Object altered with compilation errors.</error>
               </testcase>
             </testsuite>"""

  protected static final String DOUBLE_BETWNSTR_XML_RESULTS = """
            <testsuite name="UT_BETWNSTR" tests="2" failures="0" skipped="0" errors="0" time="0.123">
              <testcase name="null end" classname="BETWNSTR.UT_BETWNSTR" time="0" />
              <testcase name="normal" classname="BETWNSTR.UT_BETWNSTR" time="0" />
            </testsuite>
    """
}
