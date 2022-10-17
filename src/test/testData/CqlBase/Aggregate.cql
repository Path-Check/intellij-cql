library Agregate

define Seq:
  {1, 2, 3}

define One: 
  {1, 2, 3, 4, 5}

define Factorial:
  Seq Seq
    aggregate R starting 1: R * Seq

define Factorial2:
  One X
    aggregate R starting 1: R * X
