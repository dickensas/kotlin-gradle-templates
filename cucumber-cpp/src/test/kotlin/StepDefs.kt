import io.cucumber.java.en.Then
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.core.api.Scenario
import io.cucumber.java8.En
import junit.framework.Assert.assertEquals

import org.openqa.selenium.By

class StepDefs: En {
    private lateinit var today: String
    private lateinit var actualAnswer: String
    var lib = Library()
    
    companion object {
        init {
            System.loadLibrary("cpplib")
        }
    }
    
    init {
		
	    Given("I am on the Notepad++") {
            
       }
   
       When("I select and copy entire line and copy") {
          Thread.sleep(4000)
          lib.sendSpecial(0x24)
          lib.sendShiftSpecial(0x23)
          lib.sendCtrlKey('C')
          lib.sendSpecial(0x23)
          lib.sendKey('\n')
          lib.sendCtrlKey('V')
       }
   
       Then("clipboard should not be empty") {
          
       }
   
       After { scenario: Scenario ->
          
       }
	 }	
}