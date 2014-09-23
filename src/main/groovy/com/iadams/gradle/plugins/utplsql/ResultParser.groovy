package com.iadams.gradle.plugins.utplsql

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MatchGenerator

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
    static TestDescription ParseDescription(def description) throws ResultParserException{

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
        }
        else {
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
    static String fetchDesc(String[] desc, int startIndex)
    {
        def description = ''
        (startIndex..desc.size() - 1).each{
            description += desc[it]
        }
        description.trim()
    }
}
