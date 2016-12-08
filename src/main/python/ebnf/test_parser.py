
from Lexer import *
from Parser import *
import unittest


class TestParser(unittest.TestCase):
    def init_parser(self, text):
        return Parser(Lexer(text))

    def test_one_rule(self):
        tree = self.init_parser('PROGRAM = RULE, {RULE};').parse()
        self.assertEqual(Program, type(tree))
        self.assertEqual(1, tree.rules.__len__())

    def test_two_rules(self):
        tree = self.init_parser('PROGRAM = RULE, {RULE}; RULE = A;').parse()
        self.assertEqual(Program, type(tree))
        self.assertEqual(2, tree.rules.__len__())

    def test_error_on_missing_left_side(self):
        with self.assertRaises(Exception):
            self.init_parser('PROGRAM = ;').parse()

    def test_error_on_missing_comma(self):
        with self.assertRaises(Exception):
            self.init_parser('PROGRAM = A A;').parse()

    def test_error_on_missing_closing_bracket(self):
        with self.assertRaises(Exception):
            self.init_parser('PROGRAM = [A;').parse()

    def test_longer_input(self):
        input = 'A = B, C;' \
                'B = [A], D;' \
                'C = D, {2 * D};' \
                'D = A;'
        tree = self.init_parser(input).parse()
        self.assertEqual(4, tree.rules.__len__())

    # def test_example_file(self):
    #    with open('ebnf_grammar.txt', 'r') as example:
    #        tree = self.init_parser(example.read()).parse()
    #        self.assertEqual(Program, type(tree))
    #        self.assertEqual(6, tree.rules.__len__())
