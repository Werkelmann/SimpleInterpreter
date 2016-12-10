
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

    def test_skipping(self):
        lexer = Lexer('   \n   # asd # \t')

        token = lexer.get_next_token()
        self.assertEquals(EOF, token.type)
        self.assertEquals('EOF', token.value)

    def test_line_with_quotation_marks(self):
        lexer = Lexer('A = B, "=", C, ";";')

        token = lexer.get_next_token()
        self.assertEquals(IDENTIFIER, token.type)
        self.assertEquals('A', token.value)

        token = lexer.get_next_token()
        self.assertEquals(EQUAL, token.type)
        self.assertEquals('EQUAL', token.value)

        token = lexer.get_next_token()
        self.assertEquals(IDENTIFIER, token.type)
        self.assertEquals('B', token.value)

        token = lexer.get_next_token()
        self.assertEquals(COMMA, token.type)
        self.assertEquals('COMMA', token.value)

        token = lexer.get_next_token()
        self.assertEquals(QUOTATION_MARK, token.type)
        self.assertEquals('QUOTATION_MARK', token.value)

        token = lexer.get_next_token()
        self.assertEquals(IDENTIFIER, token.type)
        self.assertEquals('=', token.value)

        token = lexer.get_next_token()
        self.assertEquals(QUOTATION_MARK, token.type)
        self.assertEquals('QUOTATION_MARK', token.value)

        token = lexer.get_next_token()
        self.assertEquals(COMMA, token.type)
        self.assertEquals('COMMA', token.value)

        token = lexer.get_next_token()
        self.assertEquals(IDENTIFIER, token.type)
        self.assertEquals('C', token.value)

        token = lexer.get_next_token()
        self.assertEquals(COMMA, token.type)
        self.assertEquals('COMMA', token.value)

        token = lexer.get_next_token()
        self.assertEquals(QUOTATION_MARK, token.type)
        self.assertEquals('QUOTATION_MARK', token.value)

        token = lexer.get_next_token()
        self.assertEquals(IDENTIFIER, token.type)
        self.assertEquals(';', token.value)

        token = lexer.get_next_token()
        self.assertEquals(QUOTATION_MARK, token.type)
        self.assertEquals('QUOTATION_MARK', token.value)

        token = lexer.get_next_token()
        self.assertEquals(SEMICOLON, token.type)
        self.assertEquals('SEMICOLON', token.value)

        token = lexer.get_next_token()
        self.assertEquals(EOF, token.type)
        self.assertEquals('EOF', token.value)

    def test_bug_with_quotation(self):
        lexer = Lexer('A = "word" B;')

        token = lexer.get_next_token()
        self.assertEquals(IDENTIFIER, token.type)
        self.assertEquals('A', token.value)

        token = lexer.get_next_token()
        self.assertEquals(EQUAL, token.type)
        self.assertEquals('EQUAL', token.value)

        token = lexer.get_next_token()
        self.assertEquals(QUOTATION_MARK, token.type)
        self.assertEquals('QUOTATION_MARK', token.value)

        token = lexer.get_next_token()
        self.assertEquals(IDENTIFIER, token.type)
        self.assertEquals('word', token.value)

        token = lexer.get_next_token()
        self.assertEquals(QUOTATION_MARK, token.type)
        self.assertEquals('QUOTATION_MARK', token.value)

        token = lexer.get_next_token()
        self.assertEquals(IDENTIFIER, token.type)
        self.assertEquals('B', token.value)

        token = lexer.get_next_token()
        self.assertEquals(SEMICOLON, token.type)
        self.assertEquals('SEMICOLON', token.value)

        token = lexer.get_next_token()
        self.assertEquals(EOF, token.type)
        self.assertEquals('EOF', token.value)

    def test_alternatives(self):
        lexer = Lexer('A | B')

        token = lexer.get_next_token()
        self.assertEquals(IDENTIFIER, token.type)
        self.assertEquals('A', token.value)

        token = lexer.get_next_token()
        self.assertEquals(ALTERNATIVE, token.type)
        self.assertEquals('ALTERNATIVE', token.value)

        token = lexer.get_next_token()
        self.assertEquals(IDENTIFIER, token.type)
        self.assertEquals('B', token.value)

        token = lexer.get_next_token()
        self.assertEquals(EOF, token.type)
        self.assertEquals('EOF', token.value)
