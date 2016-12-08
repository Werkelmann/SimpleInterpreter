
from calc1 import Interpreter, Parser, Lexer
import unittest

PROGRAM = 'PROGRAM Part12;' \
          'VAR ' \
          'a : INTEGER;' \
          '' \
          'PROCEDURE P1;' \
          'VAR ' \
          'a : REAL;' \
          'k:INTEGER;' \
          '' \
          'PROCEDURE P2;' \
          'VAR ' \
          'a, z : INTEGER;' \
          'BEGIN {P2}' \
          'z := 77;' \
          'END; {P2}' \
          '' \
          'BEGIN {P1}' \
          'END;{P2}' \
          '' \
          'BEGIN {Part12}' \
          'a := 10;' \
          'END. {Part12}'


class TestPart12(unittest.TestCase):

    def init_interpreter(self, program):
        return Interpreter(Parser(Lexer(program)))

    def test_parsing(self):
        self.init_interpreter(PROGRAM).interpret()