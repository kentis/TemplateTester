package org.k1s.templateTester

import spock.lang.Specification

class JavaExecutorTest extends Specification {


  def "Test binding after a simple Java execution"(){
   given:
     def code = """
  int a = 1;
                """
     def executor = new JavaExecutor() 
   when:
     def res = executor.execute(code,[bindingElements: ['a']])

   then:
     res?.binding?.a == 1
    
  }

}
