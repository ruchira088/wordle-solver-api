package com.ruchij.services.words

import cats.MonadThrow
import com.ruchij.types.FunctionKTypes._
import fs2.{Stream, text}
import org.http4s.Method.GET
import org.http4s.Uri
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl

class WebWordList[F[_]: MonadThrow](client: Client[F]) extends WordSource[F] {
  private val clientDsl = new Http4sClientDsl[F] {}
  import clientDsl._

  override def words(key: String): Stream[F, String] =
    Stream
      .eval(Uri.fromString(key).toType[F, Throwable])
      .flatMap(uri => client.stream(GET(uri)))
      .flatMap(_.body)
      .through(text.utf8.decode)
      .through(text.lines)

}
