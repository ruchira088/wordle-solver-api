package com.ruchij.services.words

import fs2.Stream

trait WordSource[F[_]] {
  def words(key: String): Stream[F, String]
}
