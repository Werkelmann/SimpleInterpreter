
from calc1 import Interpreter, Parser, Lexer, SymbolTableBuilder
import unittest

VALID = 'PROGRAM  TEST;' \
       'VAR ' \
       'x : INTEGER;' \
       'y : REAL;' \
       '' \
       'BEGIN ' \
       '' \
       'END.'

NAMEERROR1 = 'PROGRAM NameError1;' \
             'VAR ' \
             'a : INTEGER;' \
             '' \
             'BEGIN ' \
             'a := 2 + b;' \
             'END.'

NAMEERROR2 = 'PROGRAM NameError2;' \
             'VAR ' \
             'b : INTEGER;' \
             '' \
             'BEGIN ' \
             'b := 1;' \
             'a := 2 + b;' \
             'END.'

PART11EXAMPLE = 'PROGRAM PART11;' \
                'VAR ' \
                'number : INTEGER;' \
                'a, b : INTEGER;' \
                'y : REAL;' \
                '' \
                'BEGIN{PART11} ' \
                'number := 2;' \
                'a := number ; ' \
                'b := 10 * a + 10 * number DIV 4;' \
                'y := 20 / 7 + 3.14 ' \
                'END{PART11}.'

class TestSymbolTable(unittest.TestCase):
    def init_parser(self, program):
        return Parser(Lexer(program))

    def init_interpreter(self, program):
        return Interpreter(self.init_parser(program))

    def test_table_builder(self):
        tree = self.init_parser(VALID).parse()
        symtab_builder = SymbolTableBuilder()
        symtab_builder.visit(tree)
        self.assertEqual(symtab_builder.symbol_table.__repr__(), 'Symbols: [INTEGER, REAL, <x:INTEGER>, <y:REAL>]')

    def test_error_on_undeclared_variable(self):
        with self.assertRaises(NameError):
            tree = self.init_parser(NAMEERROR1).parse()
            symtab_builder = SymbolTableBuilder()
            symtab_builder.visit(tree)

    def test_error_on_undefined_variable(self):
        with self.assertRaises(NameError):
            tree = self.init_parser(NAMEERROR2).parse()
            symtab_builder = SymbolTableBuilder()
            symtab_builder.visit(tree)

    def test_part11_example(self):
        interpreter = self.init_interpreter(PART11EXAMPLE)
        interpreter.interpret()
        self.assertEqual(interpreter.symbol_table.__repr__(), 'Symbols: [INTEGER, REAL, <number:INTEGER>, '
                                                              '<a:INTEGER>, <b:INTEGER>, <y:REAL>]')
        self.assertEqual(interpreter.GLOBAL_SCOPE['number'], 2)
        self.assertEqual(interpreter.GLOBAL_SCOPE['a'], 2)
        self.assertEqual(interpreter.GLOBAL_SCOPE['b'], 25)
        self.assertEqual(interpreter.GLOBAL_SCOPE['y'], 5.997142857142857)
