package com.ruchij.services.words

import cats.effect.kernel.{Resource, Sync}
import cats.implicits._
import cats.{Applicative, ApplicativeError}
import com.ruchij.exceptions.ResourceNotFoundException
import fs2.{Stream, text}

import java.io.InputStream

class LocalWordSource[F[_]: Sync] extends WordSource[F] {

  override def words(key: String): Stream[F, String] =
    Stream
      .resource {
        Resource.make(
          Sync[F]
            .blocking(getClass.getClassLoader.getResourceAsStream(key))
            .flatMap(
              inputStream =>
                if (inputStream == null)
                  ApplicativeError[F, Throwable]
                    .raiseError[InputStream](ResourceNotFoundException(s"Unable to find resource named $key"))
                else Applicative[F].pure(inputStream)
            )
        ) { inputStream =>
          Sync[F].blocking(inputStream.close())
        }
      }
      .flatMap { inputStream =>
        Stream.eval(Sync[F].blocking(inputStream.readAllBytes()))
      }
      .flatMap(byteArray => Stream.emits(byteArray))
      .through(text.utf8.decode)
      .through(text.lines)

}
