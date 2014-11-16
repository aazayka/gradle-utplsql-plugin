package com.iadams.gradle.plugins.utplsql.core

/**
 * Created by Iain Adams on 30/09/2014.
 */
class TestXmlFixtures {
    protected  static final String SUCCESSFUL_TEST_DESCRIPTION = """
    <testcase name="null end" classname="BETWNSTR.UT_BETWNSTR" time="0" />
    """

    protected  static final String FAILURE_TEST_DESCRIPTION = """
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

    protected static final String DOUBLE_BETWNSTR_XML_RESULTS = """
            <testsuite name="UT_BETWNSTR" tests="2" failures="0" skipped="0" errors="0" time="0.123">
              <testcase name="null end" classname="BETWNSTR.UT_BETWNSTR" time="0" />
              <testcase name="normal" classname="BETWNSTR.UT_BETWNSTR" time="0" />
            </testsuite>
    """
}