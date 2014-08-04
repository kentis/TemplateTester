package org.k1s.templateTester
import groovy.text.*
class GroovyTemplateGenerator implements TemplateGenerator{

  def generateTemplate(templatePath, params){
    def retval = null
     try{
       def tmpl = new File(templatePath).text
       SimpleTemplateEngine engine = new SimpleTemplateEngine()
       Template simpleTemplate = engine.createTemplate(tmpl)
       retval = simpleTemplate.make(params).toString()
     } catch(Exception e){
       throw new RuntimeException("Exception running template: $templatePath with $params", e)
     }
     return retval
  }
  

}
