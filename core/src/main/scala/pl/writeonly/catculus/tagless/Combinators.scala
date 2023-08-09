package pl.writeonly.catculus.tagless

//import cats.{Id, ~>}
//import cats.tagless.{autoFunctorK, finalAlg}
//
//// Zdefiniujmy algebraiczną reprezentację kombinatorów.
//@finalAlg
//trait CombinatorsAlg[F[_]] {
//  def s[A, B, C](x: F[A => B => C], y: F[A => B], z: F[A]): F[C]
//  def k[A, B](x: F[A], y: F[B]): F[A]
//  def i[A](x: F[A]): F[A]
//}
//
//// Prosty interpreter dla Id monady.
//object CombinatorsInterpreter extends CombinatorsAlg[Id] {
//  def s[A, B, C](x: Id[A => B => C], y: Id[A => B], z: Id[A]): Id[C] = x(z)(y(z))
//  def k[A, B](x: Id[A], y: Id[B]): Id[A] = x
//  def i[A](x: Id[A]): Id[A] = x
//}
//
//// Jeśli chcielibyśmy zastosować tę logikę do innej monady, `cats-tagless` umożliwia łatwe przekształcenie.
//val toOption = new (Id ~> Option) {
//  def apply[A](fa: Id[A]): Option[A] = Some(fa)
//}
//val combinatorsOption = CombinatorsInterpreter.mapK(toOption)
//
//// Testy
//import CombinatorsInterpreter._
//
//println(s(k[String, String, String], i[String], "hello"))  // powinno zwrócić "hello"
//println(combinatorsOption.s(k[String, String, String], combinatorsOption.i[String], "hello"))  // powinno zwrócić Some("hello")
