package pl.writeonly.catculator.core.adt.calculus

import mouse.all.anySyntaxMouse
import org.scalatest.prop.*
import pl.writeonly.catculator.core.LambdaConfig.*
import pl.writeonly.catculator.core.TableDrivenPropertySpec
import pl.writeonly.catculator.core.adt.calculus.Lambda.*
import pl.writeonly.catculator.core.calculators.lazyk.ADT.fromCombinatorBT
import pl.writeonly.catculator.core.calculators.lazyk.Evaluator
import pl.writeonly.catculator.core.generators.HaskellGenerator
import pl.writeonly.catculator.core.parsers.HaskellParser
import pl.writeonly.catculator.core.reducer.AbstractionReducer.reduceAbstraction
import pl.writeonly.catculator.core.reducer.FunctionReducer
import pl.writeonly.catculator.core.reducer.FunctionReducer.*
import pl.writeonly.catculator.core.reducer.LambdaReducer.toCombinatorBT
import pl.writeonly.catculator.core.reducer.SugarReducer.*

import scala.language.postfixOps

class HaskellSpec extends TableDrivenPropertySpec {

  val HelloWorld1: String = """
      |const = \ a b -> a
      |""".stripMargin

  val HelloWorld2: String = """
      |const = 0
      |main = 1
      |""".stripMargin

  val HelloWorld3: String = """
      |const = \ a b -> a
      |main = 0
      |""".stripMargin

  val HelloWorld4: String = """
      |const = \ a b -> a
      |main = const
      |""".stripMargin

  val HelloWorld41: String = """
      |const = \ a b -> a
      |main = (const 0)
      |""".stripMargin

  val HelloWorld5: String = """
      |const = \ a b -> a
      |main = (const const)
      |""".stripMargin

  val Fix: String = """
      |fix = \ f -> let x = f x in x
      |main = \ -> input
      |""".stripMargin

  val Empty: String = """
      |const = \ a b -> a
      |true = \ x y -> x
      |false = \ x y -> y
      |zero = \ f x -> x
      |succ = \ n f x -> f (n f x)
      |pair = \ x y f -> f x y
      |nil = \ x -> true
      |cons = \ -> pair
      |main = (const "")
      |""".stripMargin

  val HelloWorld: String = """
      |const = \ a b -> a
      |true = \ x y -> x
      |false = \ x y -> y
      |zero = \ f x -> x
      |succ = \ n f x -> f (n f x)
      |pair = \ x y f -> f x y
      |nil = \ x -> true
      |cons = \ -> pair
      |main = (const "Hello World!")
      |""".stripMargin

  val Factorial: String = """
      |const = \ a b -> a
      |true = \ x y -> x
      |false = \ x y -> y
      |or = \ p q -> p q p
      |and = \ p  q -> p p q
      |not = \p -> p false true
      |ifte = \ p a b -> p a b
      |iszero = \ n -> n (\ x -> false) true
      |zero = \ f x -> x
      |succ = \ n f x -> f (n f x)
      |plus = \ m n -> m succ n
      |mult = \ m n f -> m (n f)
      |pred = \ n f x -> n (\ g h -> (h (g h)) (\ u -> x) (\ u -> u))
      |sub = \ m n -> n pred m
      |leq = \ m n -> iszero (sub m n)
      |lt = \ m n -> leq (succ m) n
      |eq = \m n -> and (leq m n) (leq n m)
      |pair = \ x y f -> f x y
      |cons = \ -> pair
      |first = \ p -> p true
      |second = \ p -> p false
      |nil = \ x -> true
      |null = \ p -> p (\ x y -> false)
      |head = \ -> first
      |tail = \ -> second
      |fix = \ g -> (\ x -> g (x x)) (\ x -> g (x x))
      |zeroPair = \ -> (pair 0 0)
      |divmodFix = \ rec m n acc -> iszero (sub m n) (pair acc m) (rec (sub m n) n (pair (succ (first acc)) (sub m (mult n (succ (first acc))))))
      |divmod = \ m n -> fix divmodFix m n zeroPair
      |helperFix = \ rec m n -> iszero m nil (apply (divmod m n) (\ t -> cons (second t) (rec (first t))))
      |helper = \ -> fix helperFix
      |reverseFix = \ rec l acc -> ifte (null l) acc (rec (tail l) (cons (head l) acc))
      |reverse = \ l -> fix reverseFix l nil
      |mapFix = \ rec f l -> ifte (null l) nil (cons (f (head l)) (rec f (tail l)))
      |map = \ -> fix mapFix
      |stringFromNatural = \ n -> map (plus '0') (helper n 10)
      |parseIntFix = \ rec acc s -> apply (head s) (\ c -> (or (leq c '0') (lt '9' c)) (pair acc s) (rec (plus (mult 10 acc) (sub c '0')) (tail s)))
      |parseInt = \ -> fix parseIntFix
      |factorialFix = \ rec n -> ifte (leq n 1) 1 (mult n (rec (sub n 1)))
      |factorial = \ -> fix factorialFix
      |main = \input -> stringFromNatural (factorial (first (parseInt input)))
      |""".stripMargin

  val simpleFunctions: TableFor2[String, String] = Table(
    ("function", "lambda"),
    ("main = id", "id"),
    ("main = 0", "0"),
    ("main = \"Hello Wolrd!\"", "\"Hello Wolrd!\""),
    ("main = \\ input -> input", "\\input (input)"),
    ("main = \\ input -> \"Hello Wolrd!\"", "\\input (\"Hello Wolrd!\")"),
    ("main = \\ -> let x = 0 in x", "(let x = 0 in x)"),
    ("main = \\ f -> let x = (f x) in x", "\\f (let x = (f x) in x)"),
  )

  val helloWorldFunctions: TableFor2[String, String] = Table(
    ("function", "lambda"),
    (HelloWorld1, "`apply `\\a \\b (a) \\const main"),
    (HelloWorld2, "`apply `0 \\const `apply `1 \\main main"),
    (HelloWorld3, "`apply `\\a \\b (a) \\const `apply `0 \\main main"),
    (HelloWorld4, "`apply `\\a \\b (a) \\const `apply `const \\main main"),
    (
      HelloWorld5,
      "`apply `\\a \\b (a) \\const `apply `(const const) \\main main",
    ),
  )

  val programs: TableFor3[String, String, String] = Table(
    ("function", "input", "output"),
    (Empty, "", ""),
    (HelloWorld, "", ""),
    (Factorial, "", ""),
  )

  it should "parse sinpleFunctions and save lambda" in {
    forAll(simpleFunctions) { (function, lambda) =>
      HaskellParser
        .parseFunction(function)
        .map(_.lambda)
        .map(HaskellGenerator.generate)
        .value shouldBe lambda
    }
  }

  it should "parseFunctions helloWorldFunctions and save lambda" in {
    forAll(helloWorldFunctions) { (function, lambda) =>
      HaskellParser
        .parseFunctions(function)
        .map { xs =>
          haskellFunctionReducer.reduceFunctions(xs.toList)
        }
        .map(HaskellGenerator.generate)
        .value shouldBe lambda
    }
  }

  it should "parseFunctions programs and save combinators" in {
    forAll(programs) { (function, _, _) =>

      val parsedLambda = HaskellParser
        .parseFunctions(function)
        .map { xs =>
          haskellFunctionReducer.reduceFunctions(xs.toList)
        }
        .map(haskellConfig.addApply)
        .map(haskellSugarReducer.reduceSugar)
        .map(reduceAbstraction)
        .value

      val combinatorsEither = parsedLambda |> toCombinatorBT
      assert(combinatorsEither.isRight, s"$combinatorsEither")
    }
  }

  it should "parseFunctions programs and save combinators 2" in {
    forAll(programs) { (function, i, o) =>

      val parsedLambda = HaskellParser
        .parseFunctions(function)
        .map { xs =>
          haskellFunctionReducer.reduceFunctions(xs.toList)
        }
        .map(haskellConfig.addApply)
        .map(haskellSugarReducer.reduceSugar)
        .map(reduceAbstraction)
        .value

      val combinatorsEither = parsedLambda |> toCombinatorBT
      val aaa = fromCombinatorBT(combinatorsEither.value)

      Evaluator.evalCombinator(aaa, i)

      assert(combinatorsEither.isRight, s"$combinatorsEither")
    }
  }
}
