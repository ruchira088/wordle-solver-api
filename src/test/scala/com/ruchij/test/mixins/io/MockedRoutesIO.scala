package com.ruchij.test.mixins.io

import cats.effect.IO
import cats.effect.kernel.Async
import com.ruchij.test.mixins.MockedRoutes
import fs2.compression.Compression
import org.scalatest.TestSuite

trait MockedRoutesIO extends MockedRoutes[IO] { self: TestSuite =>
  override val async: Async[IO] = IO.asyncForIO
  override val compression: Compression[IO] = Compression.forIO
}
