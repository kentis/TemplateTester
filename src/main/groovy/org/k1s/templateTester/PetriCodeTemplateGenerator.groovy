package org.k1s.templateTester

class PetriCodeTemplateGenerator implements TemplateGenerator {
  
  GroovyTemplateGenerator groovyTmplGen = new GroovyTemplateGenerator()

  def generateTemplate(templatePath, params){
    //Generate the template
    def retval = groovyTmplGen.generateTemplate(templatePath, params)
 
    //Remove tags
    retval = retval.replaceAll("%%VARS:.*%%","" )
    retval = retval.replaceAll("%%yield.*%%","" )

    
    return retval
  }
  

}
