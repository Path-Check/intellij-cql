library TestDef

define Seq:
  { 1, 2, 3, 4, 5 }

define FactorialOfFive:
  Seq Num
    aggregate Result starting 1: Result * Num

define function Sum(y Decimal, m Decimal): y + m

// @test
define myTest: FactorialOfFive

// @test
define myTest2: Sum(1, 2)