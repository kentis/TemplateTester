
import java.lang.annotation.*
import spock.lang.Specification


import org.k1s.templateTester.*

class SetFieldJavaTest extends TemplateSpecification {

    def setup() {
        generator = new PetriCodeTemplateGenerator()
        executor = new JavaExecutor();
        useResources = true;
    } 

  def "Simple syntax"() {
    setup:
      //println this.methods()
      def text = genTemplate("./jTmpls/setField.tmpl", [params: ['fieldName','1']])
    expect:
      text.contains("this.fieldName = 1")
  }

  def "Conditioned syntax"() {
    setup:
      //println this.methods()
      def text = genTemplate("jTmpls/setField.tmpl", [params: ['fieldName','1'], cond: ['cond'], condTrueExpr: '1', condFalseExpr: '2'])
    expect:
      text.contains("this.fieldName = 2")
      text.contains("if(cond)")
  }


  def "Simple valid"(){
    expect:
      checkTemplateSyntax("jTmpls/setField.tmpl", [params: ['fieldName','1']])
  }

  def "Conditioned valid"(){
    expect:
      checkTemplateSyntax("jTmpls/setField.tmpl", [params: ['fieldName','1'], cond: ['cond'], condTrueExpr: '1', condFalseExpr: '2'])
  }

  def "Simple Semantic"(){
    when:
      def script = genTemplate("jTmpls/setField.tmpl",[params: ['fieldName','1']])
      def res = executor.execute(script,[bindingElements: ['fieldName'], fields: [['Integer', 'fieldName']]])
    then:
      res.binding['fieldName'] == 1
  }


  def "Conditioned Semantic True"(){
    when:
      def script  = genTemplate("jTmpls/setField.tmpl",[params: ['fieldName','1'], cond: ['true'], condTrueExpr: '1', condFalseExpr: '2'])
      def res = executor.execute(script, [bindingElements: ['fieldName'], fields: [['Integer', 'fieldName']]] )
    then:
      res.binding['fieldName'] == 1
  }

  def "Conditioned Semantic False"(){
    when:
      def script  = genTemplate("jTmpls/setField.tmpl",[params: ['fieldName','1'], cond: ['false'], condTrueExpr: '1', condFalseExpr: '2'])
      def res = executor.execute(script, [bindingElements: ['fieldName'], fields: [['Integer', 'fieldName']]] )
    then:
      res.binding['fieldName'] == 2
  }



}
