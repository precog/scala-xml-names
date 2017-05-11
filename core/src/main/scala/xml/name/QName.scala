/*
 * Copyright 2014–2017 SlamData Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xml.name

import slamdata.Predef._
import xml.name.validate._

import eu.timepit.refined.refineV
import monocle.Prism
import monocle.macros.Lenses
import scalaz.{Order, Show}
import scalaz.std.option._
import scalaz.std.tuple._
import scalaz.syntax.apply._
import scalaz.syntax.show._

@Lenses
final case class QName(prefix: Option[NSPrefix], localPart: NCName) {
  override def toString = this.shows
}

object QName extends QNameInstances {
  def prefixed(prefix: NSPrefix, localPart: NCName): QName =
    QName(Some(prefix), localPart)

  def unprefixed(localPart: NCName): QName =
    QName(None, localPart)

  val string: Prism[String, QName] = {
    def asNCName(s: String): Option[NCName] =
      refineV[IsNCName](s).right.toOption map (NCName(_))

    Prism((_: String).split(':') match {
      case Array(pfx, loc) => (asNCName(pfx) |@| asNCName(loc))((p, l) => QName.prefixed(NSPrefix(p), l))
      case Array(loc)      => asNCName(loc) map (QName.unprefixed)
      case _               => None
    })(_.shows)
  }
}

sealed abstract class QNameInstances {
  implicit val order: Order[QName] =
    Order.orderBy(qn => (qn.prefix, qn.localPart))

  implicit val show: Show[QName] =
    Show.shows(qn => qn.prefix.fold(qn.localPart.shows)(p => s"${p.shows}:${qn.localPart.shows}"))
}
