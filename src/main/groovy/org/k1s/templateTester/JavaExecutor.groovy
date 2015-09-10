package org.k1s.templateTester

import groovy.text.*

class JavaExecutor {

    final def TEMP_DIR = "temp"

    def tmpl = '''
        public class JavaExec {
          ${fields}
          public static void main(String[] args){
            new JavaExec().theMethod();
          }
          
          public void theMethod(){
            ${setup}
            ${templateText}
            ${teardown}


          }  
          
        }
    '''


    def execute(setup, templateText, teardown,fields =""){
      def code = new SimpleTemplateEngine()
          .createTemplate( tmpl )
          .make( [setup: setup, templateText:templateText, teardown:teardown, fields:fields] )
          .toString()
      def workDir = File.createTempDir() //TEMP_DIR)
      workDir.mkdir()
      workDir.deleteOnExit()  
      	
      def javaFile = new File(workDir, "JavaExec.java") //File(TEMP_DIR+File.separator+"JavaExec.java")
      javaFile.write code

      def proc = "javac JavaExec.java".execute(null, workDir)
      proc.waitFor()
      if(proc.exitValue() != 0) {
         println proc.exitValue()
         println proc.err.text
         println proc.in.text
         throw new RuntimeException("Compile of Java class failed: ${code}")
      }
      
      proc = "java JavaExec".execute(null, workDir)
      proc.waitFor()
      
      //get the last line
      def lastline
      proc.in.text.eachLine{
        lastline = it
      }
      def mymap = new GroovyShell().evaluate(lastline)
      return  [binding: mymap]
      //println proc.in.text
    }
  
    def execute(templateText, params){
      def fields = ""
      params.fields?.each {
	fields += "${it[0]} ${it[1]};\n"
      }
      
      def teardown = "StringBuilder _bindings = new StringBuilder();\n"
      params.bindingElements.each{
        teardown += "_bindings.append(\"$it: \").append($it).append(\",\");\n"
      }
      teardown += 'System.out.println("["+_bindings.toString()+"]");'
        
      return execute("", templateText, teardown, fields) 
   }


   def checkValid(text){
     new GroovyShell().parse(text)
     return true
   }
}
