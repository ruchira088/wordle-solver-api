package com.ruchij

import cats.effect.{Async, ExitCode, IO, IOApp}
import cats.implicits._
import com.ruchij.config.ServiceConfiguration
import com.ruchij.services.health.{HealthService, HealthServiceImpl}
import com.ruchij.services.solver.{LocalWordleSolver, WordleSolver}
import com.ruchij.services.words.{LocalWordSource, WordSource}
import com.ruchij.web.Routes
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder
import pureconfig.ConfigSource

object App extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    for {
      configObjectSource <- IO.delay(ConfigSource.defaultApplication)
      serviceConfiguration <- ServiceConfiguration.parse[IO](configObjectSource)

      healthService = new HealthServiceImpl[IO](serviceConfiguration.buildInformation)
      wordSource = new LocalWordSource[IO]

      httpApp <- httpApplication(wordSource, healthService, serviceConfiguration)

      exitCode <- BlazeServerBuilder[IO]
        .withHttpApp(httpApp)
        .bindHttp(serviceConfiguration.httpConfiguration.port, serviceConfiguration.httpConfiguration.host)
        .serve
        .compile
        .lastOrError

    } yield exitCode

  def httpApplication[F[_]: Async](
    wordSource: WordSource[F],
    healthService: HealthService[F],
    serviceConfiguration: ServiceConfiguration
  ): F[HttpApp[F]] =
    wordSource
      .words(serviceConfiguration.wordSourceConfiguration.key)
      .compile
      .toList
      .map { words =>
        val wordleSolver: WordleSolver[F] = new LocalWordleSolver[F](words)

        Routes(wordleSolver, healthService)
      }

}
