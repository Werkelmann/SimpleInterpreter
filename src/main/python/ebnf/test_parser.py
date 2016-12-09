
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

    def test_longer_prog(self):
        prog = 'A = B, C;' \
                'B = [A], D;' \
                'C = D, {2 * D};' \
                'D = A;'
        tree = self.init_parser(prog).parse()
        self.assertEqual(4, tree.rules.__len__())

    def test_example_line_1(self):
        prog = 'PROGRAM = RULE, {RULE};                                     # 8 #'
        tree = self.init_parser(prog).parse()
        self.assertEqual(1, tree.rules.__len__())

    def test_example_line_2(self):
        prog = 'RULE = IDENTIFIER, "=", DEFINITION, ";";                    # 22 #'
        tree = self.init_parser(prog).parse()
        self.assertEqual(1, tree.rules.__len__())

    # def test_example_line_3(self):
    #     prog = 'DEFINITION = SYMBOLLIST, {"|", SYMBOLLIST};               # 34 #'
    #     tree = self.init_parser(prog).parse()
    #     self.assertEqual(1, tree.rules.__len__())
    #
    # def test_example_line_4(self):
    #     prog = 'SYMBOL = P_SIGN | "[", P_SIGN, "]" | "{", P_SIGN, "}";      # 66 #'
    #     tree = self.init_parser(prog).parse()
    #     self.assertEqual(1, tree.rules.__len__())
    #
    # def test_example_line_5(self):
    #     prog = 'P_SIGN = [NUMBER, "*"], SIGN;                               # 78 #'
    #     tree = self.init_parser(prog).parse()
    #     self.assertEqual(1, tree.rules.__len__())
    #
    # def test_example_line_6(self):
    #     prog = 'SIGN = IDENTIFIER | "QUOT" IDENTIFIER "QUOT";               # 90 QUOT = "#'
    #     tree = self.init_parser(prog).parse()
    #     self.assertEqual(1, tree.rules.__len__())
    #
    # def load_file(self):
    #     try:
    #         return open('src/main/python/ebnf/ebnf_grammar.txt', 'r')
    #     except FileNotFoundError as err:
    #         return open('ebnf_grammar.txt', 'r')
    #
    # def test_example_file(self):
    #     example = self.load_file()
    #     tree = self.init_parser(example.read()).parse()
    #     example.close()
    #     self.assertEqual(Program, type(tree))
    #     self.assertEqual(6, tree.rules.__len__())

