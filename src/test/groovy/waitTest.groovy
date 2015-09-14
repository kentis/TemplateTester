import java.lang.annotation.*

import spock.lang.Specification
import org.k1s.templateTester.TemplateSpecification

class WaitTest extends TemplateSpecification {

  def "Test wait Syntax"() {
    setup:
      //println this.methods()
      useResources = true
      def text = genTemplate("tmpls/wait.tmpl", [params: [1000]])
    expect:
      text.contains("Thread.sleep(1000)")
  }

  def "Wait Syntax is valid"(){
      useResources = true

    expect:
      checkTemplateSyntax("tmpls/wait.tmpl", [params: [1000]])
  }

  def "Wait Semantic"(){
    setup:
      useResources = true

      def time = System.currentTimeMillis() 
    when:
      getExecutableTemplate("tmpls/wait.tmpl",[params: [5000]]).run()
    then:
      System.currentTimeMillis() >= (time + 5000)
  }
}
