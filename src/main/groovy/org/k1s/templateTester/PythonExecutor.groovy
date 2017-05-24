package org.k1s.templateTester

import groovy.text.*

class PythonExecutor {

    final def TEMP_DIR = "temp"

    def tmpl = 
'''
class PyExec:
${fields}
  def theMethod(self):
${setup? setup + '\n':''}${templateText?templateText+'\n':''}${teardown?teardown+'\n':''}

PyExec().theMethod()
'''


    def execute(setup, templateText, teardown,fields =""){
      println "templateText: $templateText"
      def code = new SimpleTemplateEngine()
          .createTemplate( tmpl )
          .make( [setup: setup, templateText:templateText, teardown:teardown, fields:fields] )
          .toString()
      println "code: $code"
      def workDir = File.createTempDir() //TEMP_DIR)
      workDir.mkdir()
      workDir.deleteOnExit()  
      	
      def codeFile = new File(workDir, "PyExec.py") //File(TEMP_DIR+File.separator+"JavaExec.java")
      codeFile.write code

      def proc = "python -m py_compile PyExec.py".execute(null, workDir)
      proc.waitFor()
      if(proc.exitValue() != 0) {
         println proc.exitValue()
         println proc.err.text
         println proc.in.text
         throw new RuntimeException("Compile of Python script failed: ${code}")
      }
      
      proc = "python PyExec.py".execute(null, workDir)
      proc.waitFor()
      
      //get the last line
      def lastline
      proc.in.text.eachLine{
        lastline = it
      }
      if(lastline == null)
        throw new Exception("no output")
      def mymap = new GroovyShell().evaluate(lastline)
      return  [binding: mymap]
      //println proc.in.text
    }

    def checkValid(code){
      //code = indent(code,0)
      def workDir = File.createTempDir() //TEMP_DIR)
      workDir.mkdir()
      workDir.deleteOnExit()

      def codeFile = new File(workDir, "PyExec.py") //File(TEMP_DIR+File.separator+"JavaExec.java")
      codeFile.write code

      def proc = "python -m py_compile PyExec.py".execute(null, workDir)
      proc.waitFor()
      if(proc.exitValue() != 0) {
         println proc.exitValue()
         println proc.err.text
         println proc.in.text
         throw new RuntimeException("Compile of Python script failed: ${code}")
      }
      return true
    }
 
    def execute(templateText, params){
      def fields = ""
      params.fields?.each {
	fields += "  ${it[1]} = ${it[2]}"
      }
      
      StringBuilder _bindings = new StringBuilder()
      params.bindingElements.each{
        _bindings.append("$it: '+str(+self.").append(it).append(")+','")
      }
      def teardown = "    print ('["+_bindings.toString()+"+']')"
        
      return execute("", templateText, teardown, fields) 
   }

  def indent(text, indent){
    def retval = new StringBuilder()
    text.eachLine{ line ->
	line = line.trim()
        //ignore empty lines
        if(line != ""){
	   retval.append(indent).append(line).append('\n')
        }
    }

  }

}
