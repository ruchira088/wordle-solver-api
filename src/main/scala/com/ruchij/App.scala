package com.ruchij

import cats.effect.{ExitCode, IO, IOApp}
import com.ruchij.config.ServiceConfiguration
import com.ruchij.services.health.HealthServiceImpl
import com.ruchij.services.solver.{LocalWordleSolver, WordleSolver}
import com.ruchij.services.words.{LocalWordSource, WebWordList, WordSource}
import com.ruchij.web.Routes
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.blaze.server.BlazeServerBuilder
import pureconfig.ConfigSource

object App extends IOApp {
  private val MitWordsList = "https://www.mit.edu/~ecprice/wordlist.10000"
  private val MieliestronkWordsList = "http://www.mieliestronk.com/corncob_lowercase.txt"
  private val DwylWordsList = "https://raw.githubusercontent.com/dwyl/english-words/master/words_alpha.txt"

  override def run(args: List[String]): IO[ExitCode] =
    for {
      configObjectSource <- IO.delay(ConfigSource.defaultApplication)
      serviceConfiguration <- ServiceConfiguration.parse[IO](configObjectSource)

      healthService = new HealthServiceImpl[IO](serviceConfiguration.buildInformation)

      exitCode <-
        BlazeClientBuilder[IO].resource.use { client =>
          val wordSource: WordSource[IO] = new WebWordList[IO](client)

          wordSource.words(MieliestronkWordsList).compile.toList
            .flatMap { wordsList =>
              val wordleSolver: WordleSolver[IO] = new LocalWordleSolver[IO](wordsList.sorted)

              BlazeServerBuilder[IO]
                .withHttpApp(Routes(wordleSolver, healthService))
                .bindHttp(serviceConfiguration.httpConfiguration.port, serviceConfiguration.httpConfiguration.host)
                .serve.compile.lastOrError
            }
        }
    }
    yield exitCode
}
