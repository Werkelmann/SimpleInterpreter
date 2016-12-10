from Lexer import Lexer
from Parser import Parser
from Interpreter import NonTerminalList, Validator
import unittest

PROG_1 = 'A = B; B = "test";'
PROG_2 = 'A = B C; B = "test" A; C = A B C;'
PROG_INVALID = 'A = B;'
PROG_ONE_LINE = 'A = "test";'


class TestValidator(unittest.TestCase):
    def test_valid_program_1(self):
        symbol_list = NonTerminalList()
        symbol_list.insert_left('A')
        symbol_list.insert_right('B')
        symbol_list.insert_left('B')
        self.assertTrue(symbol_list.is_valid())

    def test_valid_program_2(self):
        symbol_list = NonTerminalList()
        symbol_list.insert_left('A')
        symbol_list.insert_right('B')
        symbol_list.insert_right('C')
        symbol_list.insert_left('B')
        symbol_list.insert_right('A')
        symbol_list.insert_left('C')
        symbol_list.insert_right('A')
        symbol_list.insert_right('B')
        symbol_list.insert_right('C')
        self.assertTrue(symbol_list.is_valid())

    def test_error_on_missing_left_definition(self):
        with self.assertRaises(Exception):
            symbol_list = NonTerminalList()
            symbol_list.insert_left('A')
            symbol_list.insert_right('B')
            self.assertTrue(symbol_list.is_valid())

    def test_one_line_definition(self):
        symbol_list = NonTerminalList()
        symbol_list.insert_left('A')
        self.assertTrue(symbol_list.is_valid())

    def test_validator_for_program_1(self):
        tree = Parser(Lexer(PROG_1)).parse()
        self.assertTrue(Validator(tree).validate())

    def test_validator_for_program_2(self):
        tree = Parser(Lexer(PROG_2)).parse()
        self.assertTrue(Validator(tree).validate())

    def test_validator_error_on_invalid_program(self):
        with self.assertRaises(Exception):
            tree = Parser(Lexer(PROG_INVALID)).parse()
            self.assertTrue(Validator(tree).validate())

    def test_validator_for_one_line(self):
        tree = Parser(Lexer(PROG_ONE_LINE)).parse()
        self.assertTrue(Validator(tree).validate())
