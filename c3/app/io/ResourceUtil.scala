package io

object ResourceUtil {
  def using[T <: { def close() }](resource: T)(block: T => Unit) =
    try {
      block(resource)
    } finally {
      if (resource != null) resource.close()
    }
}
