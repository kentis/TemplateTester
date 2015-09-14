import org.k1s.templateTester.*
import java.lang.annotation.*

import spock.lang.Specification


class SetFieldTest extends TemplateSpecification {

    def setup() {
        generator = new PetriCodeTemplateGenerator()
        useResources = true
    } 

  def "Simple syntax"() {
    setup:
      //println this.methods()
      def text = genTemplate("tmpls/setField.tmpl", [params: ['fieldName','1']])
    expect:
      text.contains("this.fieldName = 1")
  }

  def "Conditioned syntax"() {
    setup:
      //println this.methods()
      def text = genTemplate("tmpls/setField.tmpl", [params: ['fieldName','1'], cond: ['cond'], condTrueExpr: '1', condFalseExpr: '2'])
    expect:
      text.contains("this.fieldName = 1")
      text.contains("if(cond)")
  }


  def "Simple valid"(){
    expect:
      checkTemplateSyntax("tmpls/setField.tmpl", [params: ['fieldName','1']])
  }

  def "Conditioned valid"(){
    expect:
      checkTemplateSyntax("tmpls/setField.tmpl", [params: ['fieldName','1'], cond: ['cond'], condTrueExpr: '1', condFalseExpr: '2'])
  }

  def "Simple Semantic"(){
    when:
      def script = getExecutableTemplate("tmpls/setField.tmpl",[params: ['fieldName','1']])
      script.run()
    then:
      script.binding.getVariable('fieldName') == 1
  }


  def "Conditioned Semantic True"(){
    when:
      def script  = getExecutableTemplate("tmpls/setField.tmpl",[params: ['fieldName','1'], cond: ['true'], condTrueExpr: '1', condFalseExpr: '2'])
      script.run()
    then:
      script.binding.getVariable('fieldName') == 1
  }

  def "Conditioned Semantic False"(){
    when:
      def script  = getExecutableTemplate("tmpls/setField.tmpl",[params: ['fieldName','1'], cond: ['false'], condTrueExpr: '1', condFalseExpr: '2'])
      script.run()
      //println genTemplate("tmpls/setField.tmpl",[params: ['fieldName','1'], cond: ['false'], condTrueExpr: '1', condFalseExpr: '2'])
    then:
      script.binding.getVariable('fieldName') == 2
  }



}
