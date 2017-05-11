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

import scalaz.ISet
import scalaz.std.anyVal._
import scalaz.std.iterable._

package object validate {
  /** @see https://www.w3.org/TR/2008/REC-xml-20081126/#NT-Name
    *
    * TODO: There are likely faster ways of defining this if lookup performance
    *       ever becomes an issue.
    */
  val NameStartChars: ISet[Char] =
    ISet.singleton(   ':'                 ) union
    ISet.fromFoldable('A'      to 'Z'     ) union
    ISet.singleton(   '_'                 ) union
    ISet.fromFoldable('a'      to 'z'     ) union
    ISet.fromFoldable('\u00C0' to '\u00D6') union
    ISet.fromFoldable('\u00D8' to '\u00F6') union
    ISet.fromFoldable('\u00F8' to '\u02FF') union
    ISet.fromFoldable('\u0370' to '\u037D') union
    ISet.fromFoldable('\u037F' to '\u1FFF') union
    ISet.fromFoldable('\u200C' to '\u200D') union
    ISet.fromFoldable('\u2070' to '\u218F') union
    ISet.fromFoldable('\u2C00' to '\u2FEF') union
    ISet.fromFoldable('\u3001' to '\uD7FF') union
    ISet.fromFoldable('\uF900' to '\uFDCF') union
    ISet.fromFoldable('\uFDF0' to '\uFFFD')

  val NameChars: ISet[Char] =
    NameStartChars                          union
    ISet.singleton(   '-'                 ) union
    ISet.singleton(   '.'                 ) union
    ISet.fromFoldable('0'      to '9'     ) union
    ISet.singleton(   '\u00B7'            ) union
    ISet.fromFoldable('\u0300' to '\u036F') union
    ISet.fromFoldable('\u203F' to '\u2040')


  /** @see https://www.w3.org/TR/2009/REC-xml-names-20091208/#NT-NCName */
  val NCNameStartChars: ISet[Char] =
    NameStartChars delete ':'

  val NCNameChars: ISet[Char] =
    NameChars delete ':'
}
