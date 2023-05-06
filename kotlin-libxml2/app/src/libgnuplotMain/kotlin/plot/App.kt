package plot

import kotlin.native.concurrent.*
import platform.posix.sleep
import kotlinx.cinterop.*
import kotlin.text.*
import libxslt.*


fun main() {
    var xml = 
        """
	    <books>
	      <book>Book 1</book>
	      <book>Book 2</book>
	    </books>
	    """.encodeToByteArray().toUByteArray().toCValues()
	var xmlDoc = xmlReadDoc(xml, null, null, 0)
    
    var xsl = 
        """
	    <xsl:stylesheet
         version='1.0'
         xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
          <xsl:template match='books'>
            <xsl:for-each select='//book[2]'>
              <xsl:sort lang='en'/>
              <xsl:copy-of select='.'/>
            </xsl:for-each>
          </xsl:template>
        </xsl:stylesheet>
	    """.encodeToByteArray().toUByteArray().toCValues()
    var xslDoc = xmlReadDoc(xsl, null, null, 0)
    
    var style = xsltParseStylesheetDoc(xslDoc)
    if (style == null)
        xmlFreeDoc(xslDoc)
    var tctxt = xsltNewTransformContext(style, xmlDoc)
    var resultDoc = xsltApplyStylesheetUser(style, xmlDoc, null, null, null, tctxt)
    xsltFreeTransformContext(tctxt)
    
    xsltSaveResultToFile(stdout, resultDoc, style)
    
    xmlFreeDoc(resultDoc)
    xsltFreeStylesheet(style)
    xmlFreeDoc(xmlDoc)
}