package utils.pageobjects.replica

import utils.WithBrowser
import utils.pageobjects.{PageObjectsContext,ClaimPage,PageContext}

final class ReplicaPage(ctx:PageObjectsContext)extends ClaimPage(ctx,ReplicaPage.url){
}

object ReplicaPage{
  val url="/replica-urls"

  def apply(ctx:PageObjectsContext)=new ReplicaPage(ctx)
}

trait ReplicaPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = ReplicaPage(PageObjectsContext(browser))
}
