
package views.html

import play.templates._
import play.templates.TemplateMagic._

import play.api.templates._
import play.api.templates.PlayMagic._
import models._
import controllers._
import play.api.i18n._
import play.api.mvc._
import play.api.data._
import views.html._
/**/
object index extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template1[String,play.api.templates.Html] {

    /**/
    def apply/*1.2*/(message: String):play.api.templates.Html = {
        _display_ {

Seq[Any](format.raw/*1.19*/("""

"""),_display_(Seq[Any](/*3.2*/main("Carers Allowance")/*3.26*/ {_display_(Seq[Any](format.raw/*3.28*/("""
    
    <p>"""),_display_(Seq[Any](/*5.9*/message)),format.raw/*5.16*/("""</p>
    
""")))})),format.raw/*7.2*/("""
"""))}
    }
    
    def render(message:String): play.api.templates.Html = apply(message)
    
    def f:((String) => play.api.templates.Html) = (message) => apply(message)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Tue May 28 09:20:30 BST 2013
                    SOURCE: /Users/phil.barton/Development/DWP/ClaimCapture/app/views/index.scala.html
                    HASH: 16b60825c18fcbc0728d12add7283ff3c82aa6e6
                    MATRIX: 505->1|599->18|636->21|668->45|707->47|755->61|783->68|824->79
                    LINES: 19->1|22->1|24->3|24->3|24->3|26->5|26->5|28->7
                    -- GENERATED --
                */
            