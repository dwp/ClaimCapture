package controllers

import play.api.Play
import play.api.mvc.{Action, Controller}


object Replica extends Controller{


  def list = Action{ request =>
    val pathList = Play.current.routes.map(_.documentation.map(_._2).distinct).getOrElse(Seq())

    //This list will help us remove the paths of the urls in iterated nodes (they appear duplicated with and without the $id<[^/]+> part)
    val iteratedPaths = pathList.filter(_.matches(".*/\\$id.*")).map { p =>
      val m = "(.*)/\\$id.*".r.pattern.matcher(p)
      if(m.matches()) m.group(1) else "fail"
    }.flatMap(e => Seq(e,e+"/$id<[^/]+>"))

    val updatedList = Seq(pathList.filter(_.matches("/allowance.*")) ,
                      pathList.filter(_.matches("/your-claim-date")) ,
                      pathList.filter(_.matches("/about-you.*")) ,
                      pathList.filter(_.matches("/your-partner.*")) ,
                      pathList.filter(_.matches("/care-you-provide.*")) ,
                      pathList.filter(_.matches("/breaks.*")) ,
                      pathList.filter(_.matches("/employment.*")) ,
                      pathList.filter(_.matches("/self-employment.*")) ,
                      pathList.filter(_.matches("/other-money.*")) ,
                      pathList.filter(_.matches("/pay-details.*")) ,
                      pathList.filter(_.matches("/information.*")) ,
                      pathList.filter(_.matches("/consent-and-declaration.*")) ,
                      pathList.filter(_.matches("/preview"))
    ).map(_.filterNot(iteratedPaths.contains(_)))
     .map(_.filterNot(_.matches(".*delete.*")))
     .map(_.filterNot(_.matches(".*error.*")))
     .filterNot(_.isEmpty)

    Ok(views.html.replica.routesList(updatedList))
  }
}
