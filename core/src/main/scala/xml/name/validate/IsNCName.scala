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

package xml.name.validate

import slamdata.Predef._

import eu.timepit.refined.api.Validate
import scalaz.std.anyVal._

/** Refined predicate that checks if a `String` is a valid XML NCName.
  * @see https://www.w3.org/TR/2009/REC-xml-names-20091208/#NT-NCName
  */
sealed abstract class IsNCName()

object IsNCName extends (String => Boolean) {
  def apply(s: String): Boolean =
    s.headOption.exists(NCNameStartChars member _) && s.drop(1).forall(NCNameChars member _)

  implicit val isNCNameValidate: Validate.Plain[String, IsNCName] =
    Validate.fromPredicate(this, s => s"""IsNCName("$s")""", new IsNCName {})
}
