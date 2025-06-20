package com.ruchij.web

import cats.effect.kernel.Async
import com.ruchij.services.health.HealthService
import com.ruchij.services.solver.WordleSolver
import com.ruchij.web.middleware.{ExceptionHandler, NotFoundHandler}
import com.ruchij.web.routes.{HealthRoutes, WordleSolutionRoutes}
import fs2.compression.Compression
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.server.middleware.{CORS, GZip}
import org.http4s.{HttpApp, HttpRoutes}

object Routes {
  def apply[F[_]: Async: Compression](wordleSolver: WordleSolver[F], healthService: HealthService[F]): HttpApp[F] = {
    implicit val dsl: Http4sDsl[F] = new Http4sDsl[F] {}

    val routes: HttpRoutes[F] =
      Router("/service" -> HealthRoutes(healthService), "/solutions" -> WordleSolutionRoutes(wordleSolver))

    GZip {
      CORS.policy {
        ExceptionHandler {
          NotFoundHandler(routes)
        }
      }
    }
  }
}
