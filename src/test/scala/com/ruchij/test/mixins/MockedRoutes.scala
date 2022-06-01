package com.ruchij.test.mixins

import cats.effect.kernel.Async
import com.ruchij.services.health.HealthService
import com.ruchij.services.solver.WordleSolver
import com.ruchij.web.Routes
import org.http4s.HttpApp
import org.scalamock.scalatest.MockFactory
import org.scalatest.OneInstancePerTest

trait MockedRoutes[F[_]] extends MockFactory with OneInstancePerTest {

  val wordleSolver: WordleSolver[F] = mock[WordleSolver[F]]
  val healthService: HealthService[F] = mock[HealthService[F]]

  val async: Async[F]

  def createRoutes(): HttpApp[F] =
    Routes[F](wordleSolver, healthService)(async)

}
