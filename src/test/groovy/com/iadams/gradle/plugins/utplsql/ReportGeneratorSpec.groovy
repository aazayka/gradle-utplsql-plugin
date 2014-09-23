package com.iadams.gradle.plugins.utplsql

import spock.lang.Specification
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit

/**
 * Created by Iain Adams on 29/09/2014.
 */
class ReportGeneratorSpec extends Specification {

    def desc1, desc2

    def setup() {
        desc1 = new TestDescription()
        desc1.procedureName = "BETWNSTR.UT_BETWNSTR"
        desc1.testName = "null end"
        desc1.type = "ISNULL"
        desc1.results = "Expected \"\" and got \"\""

        desc2 = new TestDescription()
        desc2.procedureName = "BETWNSTR.UT_BETWNSTR"
        desc2.testName = "normal"
        desc2.type = "EQ"
        desc2.results = "Expected \"cde\" and got \"cde\""
    }
}
