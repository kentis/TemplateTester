
import java.lang.annotation.*
import spock.lang.Specification


import org.k1s.templateTester.*

class SetFieldPythonTest extends TemplateSpecification {

    def templatePath = "pyTmpls/setField.tmpl"
    def setup() {
        generator = new PetriCodeTemplateGenerator()
        executor = new PythonExecutor();
        useResources = true
    } 

  def "Simple syntax"() {
    setup:
      //println this.methods()
      def text = genTemplate(templatePath, [indentLevel: 1,params: ['fieldName','1']])
    expect:
      text.contains("self.fieldName = 1")
  }

  def "Conditioned syntax"() {
    setup:
      //println this.methods()
      def text = genTemplate(templatePath, [indentLevel: 1, params: ['fieldName','1'], cond: ['cond'], condTrueExpr: '1', condFalseExpr: '2'])
    expect:
      text.contains("self.fieldName = 2")
      text.contains("if(cond)")
  }


  def "Simple valid"(){
    expect:
      checkTemplateSyntax(templatePath, [indentLevel: 0,params: ['fieldName','1']])
  }

  def "Conditioned valid"(){
    expect:
      checkTemplateSyntax(templatePath, [indentLevel: 0,params: ['fieldName','1'], cond: ['cond'], condTrueExpr: '1', condFalseExpr: '2'])
  }

  def "Simple Semantic"(){
    when:
      def script = genTemplate(templatePath,[indentLevel: 2,params: ['fieldName','1']])
      def res = executor.execute(script,[bindingElements: ['fieldName'], fields: [['', 'fieldName','0']]])
    then:
      res.binding['fieldName'] == 1
  }


  def "Conditioned Semantic True"(){
    when:
      def script  = genTemplate(templatePath,[indentLevel: 2,params: ['fieldName','1'], cond: ['True'], condTrueExpr: '1', condFalseExpr: '2'])
      def res = executor.execute(script, [bindingElements: ['fieldName'], fields: [['', 'fieldName','0',]]] )
    then:
      res.binding['fieldName'] == 1
  }

  def "Conditioned Semantic False"(){
    when:
      def script  = genTemplate(templatePath,[indentLevel: 2,params: ['fieldName','1'], cond: ['False'], condTrueExpr: '1', condFalseExpr: '2'])
      def res = executor.execute(script, [bindingElements: ['fieldName'], fields: [['', 'fieldName','0']]] )
    then:
      res.binding['fieldName'] == 2
  }



}
