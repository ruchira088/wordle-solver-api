package com.ruchij.web.routes

import cats.effect.kernel.Async
import cats.implicits._
import com.ruchij.services.solver.WordleSolver
import com.ruchij.web.requests.WordleConstraintsRequest
import com.ruchij.web.responses.WordleSolutionResponse
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl

object WordleSolutionRoutes {
  def apply[F[_]: Async](wordleSolver: WordleSolver[F])(implicit dsl: Http4sDsl[F]): HttpRoutes[F] = {
    import dsl._

    HttpRoutes.of {
      case request @ POST -> Root =>
        request
          .as[WordleConstraintsRequest]
          .flatMap { wordleConstraintsRequest =>
            wordleSolver.possibleSolutions(
              wordleConstraintsRequest.length,
              wordleConstraintsRequest.excludedLetters.getOrElse(Set.empty),
              wordleConstraintsRequest.notInPosition.getOrElse(Map.empty),
              wordleConstraintsRequest.inPosition.getOrElse(Map.empty)
            )
          }
          .flatMap { solutions =>
            Ok(WordleSolutionResponse(solutions))
          }
    }
  }
}
