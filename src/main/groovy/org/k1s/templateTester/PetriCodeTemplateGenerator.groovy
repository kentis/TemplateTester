package org.k1s.templateTester

class PetriCodeTemplateGenerator implements TemplateGenerator {
  
  GroovyTemplateGenerator groovyTmplGen = new GroovyTemplateGenerator()

  def generateTemplate(templatePath, params, useResources = false){
    //Generate the template
    def retval = groovyTmplGen.generateTemplate(templatePath, params, useResources)
 
    //Remove tags
    retval = retval.replaceAll("%%VARS:.*%%","" )
    retval = retval.replaceAll("%%yield.*%%","" )

    
    return retval
  }
  

}
