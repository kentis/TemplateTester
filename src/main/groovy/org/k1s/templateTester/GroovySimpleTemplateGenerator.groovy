package org.k1s.templateTester
import groovy.text.*
class GroovyTemplateGenerator implements TemplateGenerator{

  def generateTemplate(templatePath, params, useResources = false){
    def retval = null
     try{
       def tmpl
       if(useResources){
           println templatePath
           println getClass().getClassLoader().getResourceAsStream(templatePath)
           tmpl = getClass().getClassLoader().getResourceAsStream(templatePath).text
       } else {
           tmpl = new File(templatePath).text
       }
       SimpleTemplateEngine engine = new SimpleTemplateEngine()
       Template simpleTemplate = engine.createTemplate(tmpl)
       if(params == null || params.size == 0)
           retval = simpleTemplate.make().toString()
       else
           retval = simpleTemplate.make(params).toString()
     } catch(Exception e){
       throw new RuntimeException("Exception running template: $templatePath with $params", e)
     }
     return retval
  }
  

}
