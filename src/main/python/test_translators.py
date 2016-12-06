
from calc1 import LispTranslator, RPNTranslator, Parser, Lexer
import unittest


class TestCalculator(unittest.TestCase):

    def init_rpn(self, program):
        return RPNTranslator(Parser(Lexer(program)))

    def init_lisp(self, program):
        return LispTranslator(Parser(Lexer(program)))

    def test_simple_rpn(self):
        self.assertEqual(self.init_rpn('2 + 3').interpret(), '2 3 +')
        self.assertEqual(self.init_rpn('2 + 3 + 5').interpret(), '2 3 + 5 +')
        self.assertEqual(self.init_rpn('2 + 3 * 5').interpret(), '2 3 5 * +')
        self.assertEqual(self.init_rpn('(2 + 3) * 5').interpret(), '2 3 + 5 *')

    def test_simple_lisp(self):
        self.assertEqual(self.init_lisp('2 + 3').interpret(), '(+ 2 3)')
        self.assertEqual(self.init_lisp('2 + 3 + 5').interpret(), '(+ (+ 2 3) 5)')
        self.assertEqual(self.init_lisp('2 + 3 * 5').interpret(), '(+ 2 (* 3 5))')
        self.assertEqual(self.init_lisp('(2 + 3) * 5').interpret(), '(* (+ 2 3) 5)')

    def test_examples_chapter_seven(self):
        self.assertEqual(self.init_rpn('(5 + 3) * 12 / 3').interpret(), '5 3 + 12 * 3 /')
        self.assertEqual(self.init_lisp('2 + 3').interpret(), '(+ 2 3)')
        self.assertEqual(self.init_lisp('(2 + 3 * 5)').interpret(), '(+ 2 (* 3 5))')

if __name__ == '__main__':
    unittest.main()
