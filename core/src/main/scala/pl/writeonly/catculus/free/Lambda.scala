package pl.writeonly.catculus.free

//import cats.free.Free

//sealed trait LambdaF[+A]
//
//case class VarF(name: String) extends LambdaF[Nothing]
//case class AbsF(param: String, body: LambdaF[Unit]) extends LambdaF[Unit]
//case class AppF(func: LambdaF[Unit], arg: LambdaF[Unit]) extends LambdaF[Unit]


//type LambdaFree[A] = Free[LambdaF, A]
//
//def varF(name: String): LambdaFree[Nothing] = Free.liftF(VarF(name))
//def absF(param: String, body: LambdaFree[Unit]): LambdaFree[Unit] = Free.liftF(AbsF(param, body))
//def appF(func: LambdaFree[Unit], arg: LambdaFree[Unit]): LambdaFree[Unit] = Free.liftF(AppF(func, arg))
