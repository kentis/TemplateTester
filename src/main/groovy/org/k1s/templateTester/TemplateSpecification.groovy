package org.k1s.templateTester

import spock.lang.Specification
import groovy.text.*


class TemplateSpecification extends Specification {

  def generator = new GroovyTemplateGenerator()
  def executor
  /**
   * Runs a template
   * @param template
   * @param params
   * @return the text
   */
   def genTemplate(templ, params){
     generator.generateTemplate(templ, params)
   }


  def checkTemplateSyntax(templ, params){
	def text = genTemplate(templ, params)
        if(executor != null){
          return executor.checkValid(text)
        } else {
          new GroovyShell().parse(text)
        }              
        
        return true
  }
 
 def getExecutableTemplate(templ, params){
        def text = genTemplate(templ, params)
        return new GroovyShell().parse(text)
 }

}
