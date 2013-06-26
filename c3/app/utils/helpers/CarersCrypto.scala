package utils.helpers

import play.api.{Logger, Play}

object CarersCrypto {

  def encryptAES(v: String) = {

    Play.current.mode match{
      case play.api.Mode.Dev => v
      case play.api.Mode.Test => v
      case _ => play.api.libs.Crypto.encryptAES(v)

    }
  }
}
