
from Interpreter import *
from Parser import *
from Lexer import *
import unittest


class TestLexerGenerator(unittest.TestCase):
    def init_interpreter(self, text):
        return Interpreter(Parser(Lexer(text)))

    def test_one_line(self):
        expected = ''
        ebnf = 'Test = "TEST";'
        #self.assertEqual(expected, self.init_interpreter(ebnf).interpret())
