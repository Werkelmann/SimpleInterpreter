
from Lexer import *
from Parser import *
import unittest


class TestParser(unittest.TestCase):
    def init_parser(self, text):
        return Parser(Lexer(text))

    def test_one_rule(self):
        tree = self.init_parser('PROGRAM = RULE {RULE};').parse()
        self.assertEqual(Program, type(tree))
        self.assertEqual(1, tree.rules.__len__())

    def test_two_rules(self):
        tree = self.init_parser('PROGRAM = RULE {RULE}; RULE = A;').parse()
        self.assertEqual(Program, type(tree))
        self.assertEqual(2, tree.rules.__len__())

    def test_error_on_missing_closing_bracket(self):
        with self.assertRaises(Exception):
            self.init_parser('PROGRAM = [A;').parse()

    def test_error_on_missing_opening_bracket(self):
        with self.assertRaises(Exception):
            self.init_parser('PROGRAM = A};').parse()

    def test_error_on_missing_semicolon(self):
        with self.assertRaises(Exception):
            self.init_parser('PROGRAM = A').parse()

    def test_error_on_missing_equal(self):
        with self.assertRaises(Exception):
            self.init_parser('PROGRAM A;').parse()

    def test_longer_prog(self):
        prog = 'A = B C;' \
                'B = [A] D;' \
                'C = D {D D};' \
                'D = A;'
        tree = self.init_parser(prog).parse()
        self.assertEqual(4, tree.rules.__len__())

    def test_nested_brackets(self):
        prog = 'A = {[a] b};'
        tree = self.init_parser(prog).parse()
        self.assertEqual(1, tree.rules.__len__())

    def test_comment(self):
        prog = 'A = a #asd,478/()&//(%# b # #;# # ##'
        tree = self.init_parser(prog).parse()
        self.assertEqual(1, tree.rules.__len__())

    def load_file(self):
        try:
            return open('src/main/python/ebnf/ebnf_grammar.txt', 'r')
        except FileNotFoundError as err:
            return open('ebnf_grammar.txt', 'r')

    def test_example_file(self):
        example = self.load_file()
        tree = self.init_parser(example.read()).parse()
        example.close()
        self.assertEqual(Program, type(tree))
        self.assertEqual(6, tree.rules.__len__())

