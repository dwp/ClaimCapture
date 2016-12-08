package models.domain

import app.ConfigProperties._
import models.view.CachedClaim
import org.specs2.mock.Mockito
import play.api.cache.CacheApi
import play.api.test.Helpers._
import play.api.Play.current
import utils.ClaimEncryption

import scala.reflect.ClassTag
import models.view.cache.SessionDataHandling

trait MockForm extends Mockito with SessionDataHandling {
  def cache = current.injector.instanceOf[CacheApi]

  def mockQuestionGroup[Q <: QuestionGroup](qi: QGIdentifier)(implicit classTag: ClassTag[Q]): Q = {
    val questionGroup = mock[Q]
    questionGroup.identifier returns qi
    questionGroup
  }

  def mockQuestionGroup(id: String): QuestionGroup = {
    val questionGroupIdentifier = mock[QGIdentifier]
    questionGroupIdentifier.id returns id

    val questionGroup = mock[QuestionGroup]
    questionGroup.identifier returns questionGroupIdentifier

    questionGroup
  }

  def extractCacheKey(result:scala.concurrent.Future[play.api.mvc.Result], sessionKey : String = CachedClaim.key) = {
    session(result).get(sessionKey).get
  }

  def getClaimFromCache(result:scala.concurrent.Future[play.api.mvc.Result], sessionKey : String = CachedClaim.key) = {
    ClaimEncryption.decrypt(
      getBooleanProperty("session.data.to.db") match {
        case false => cache.get[Claim]("default" + extractCacheKey(result, sessionKey)).get
        case true => load(extractCacheKey(result, sessionKey)).get
      })
  }
}
