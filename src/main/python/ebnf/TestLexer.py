
from Lexer import *
import unittest


class TestLexer(unittest.TestCase):
    def test_one_line(self):
        lexer = Lexer('PROGRAM = RULE, {RULE};')

        token = lexer.get_next_token()
        self.assertEquals(IDENTIFIER, token.type)
        self.assertEquals('PROGRAM', token.value)

        token = lexer.get_next_token()
        self.assertEquals(EQUAL, token.type)
        self.assertEquals('EQUAL', token.value)

        token = lexer.get_next_token()
        self.assertEquals(IDENTIFIER, token.type)
        self.assertEquals('RULE', token.value)

        token = lexer.get_next_token()
        self.assertEquals(COMMA, token.type)
        self.assertEquals('COMMA', token.value)

        token = lexer.get_next_token()
        self.assertEquals(BRACKET_CURLY_OPEN, token.type)
        self.assertEquals('BRACKET_CURLY_OPEN', token.value)

        token = lexer.get_next_token()
        self.assertEquals(IDENTIFIER, token.type)
        self.assertEquals('RULE', token.value)

        token = lexer.get_next_token()
        self.assertEquals(BRACKET_CURLY_CLOSE, token.type)
        self.assertEquals('BRACKET_CURLY_CLOSE', token.value)

        token = lexer.get_next_token()
        self.assertEquals(SEMICOLON, token.type)
        self.assertEquals('SEMICOLON', token.value)

        token = lexer.get_next_token()
        self.assertEquals(EOF, token.type)
        self.assertEquals('EOF', token.value)
