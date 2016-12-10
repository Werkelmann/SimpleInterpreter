from Lexer import *

EXCEPTION_PARSE = 'Error while parsing at {position}. Found: {found} Expected: {expected}'


class AST(object):
    def __str__(self):
        return '{type}: {token}'.format(
            type=type(self).__name__,
            token=self.token.__repr__()
        )

    def __repr__(self):
        return self.__str__()


class Program(AST):
    def __init__(self):
        self.rules = []

    def __str__(self):
        return 'Program: {}'.format(self.rules.__repr__())

    __repr__ = __str__


class Rule(AST):
    def __init__(self, identifier, right):
        self.identifier = identifier
        self.right = right

    def __str__(self):
        return 'Rule: {} {}'.format(self.identifier.__repr__(), self.right.__repr__())

    __repr__ = __str__


class RightSide(AST):
    def __init__(self):
        self.sequences = []

    def __str__(self):
        return 'Right: {}'.format(self.sequences.__repr__())

    __repr__ = __str__


class Sequence(AST):
    def __init__(self):
        self.values = []

    def __str__(self):
        return 'Sequence: {}'.format(self.values.__repr__())

    __repr__ = __str__


class Option(AST):
    def __init__(self, right):
        self.right = right

    def __str__(self):
        return 'Option: {}'.format(self.right.__repr__())

    __repr__ = __str__


class Repetition(AST):
    def __init__(self, right):
        self.right = right

    def __str__(self):
        return 'Repetition: {}'.format(self.right.__repr__())

    __repr__ = __str__


class Identifier(AST):
    def __init__(self, name):
        self.name = name

    def __str__(self):
        return 'Identifier: {}'.format(self.name)

    __repr__ = __str__


class Parser(object):
    def __init__(self, lexer):
        self.lexer = lexer
        self.position = 0
        self.current_token = None
        self.next_token()

    def next_token(self):
        self.current_token = self.lexer.get_next_token()
        self.position += 1

    def error(self, expected):
        raise Exception(EXCEPTION_PARSE.format(
            position=self.position,
            found=self.current_token.type,
            expected=expected
        ))

    def eat(self, *token_types):
        if self.current_token.type not in token_types:
            self.error(token_types)
        self.next_token()

    def repetition(self):
        self.eat(BRACKET_CURLY_OPEN)
        repetition = Repetition(self.right())
        self.eat(BRACKET_CURLY_CLOSE)
        return repetition

    def option(self):
        self.eat(BRACKET_SQUARE_OPEN)
        option = Option(self.right())
        self.eat(BRACKET_SQUARE_CLOSE)
        return option

    def sequence(self):
        sequence = Sequence()

        while self.current_token.type not in (EOF, ALTERNATIVE):
            if self.current_token.type == BRACKET_CURLY_OPEN:
                sequence.values.append(self.repetition())
                continue
            if self.current_token.type == BRACKET_SQUARE_OPEN:
                sequence.values.append(self.option())
                continue
            if self.current_token.type == QUOTATION_MARK:
                self.eat(QUOTATION_MARK)
                sequence.values.append(Identifier(self.current_token.value))
                self.eat(IDENTIFIER)
                self.eat(QUOTATION_MARK)
                continue
            if self.current_token.type == IDENTIFIER:
                sequence.values.append(Identifier(self.current_token.value))
                self.eat(IDENTIFIER)
                continue
            break

        return sequence

    def right(self):
        right = RightSide()
        right.sequences.append(self.sequence())

        while self.current_token.type == ALTERNATIVE:
            self.eat(ALTERNATIVE)
            right.sequences.append(self.sequence())

        return right

    def rule(self):
        identifier = Identifier(self.current_token.value)
        self.eat(IDENTIFIER)
        self.eat(EQUAL)
        right = self.right()
        self.eat(SEMICOLON)
        return Rule(identifier, right)

    def program(self):
        program = Program()
        program.rules.append(self.rule())

        while self.current_token.type != EOF:
            program.rules.append(self.rule())

        return program

    def parse(self):
        return self.program()
