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


class Rule(AST):
    def __init__(self, identifier, definition):
        self.identifier = identifier
        self.definition = definition


class Definition(AST):
    def __init__(self):
        self.symbols = []
        self.definitions = []


class Symbol(AST):
    def __init__(self, p_sign, count=1): # 0 for optional, 1 for needed, 2 for any
        self.p_sign = p_sign
        self.count = count


class Number(AST):
    def __init__(self, value):
        self.value = value


class PSign(AST):
    def __init__(self, sign, number=Number(1)):
        self.sign = sign
        self.number = number


class Sign(AST):
    def __init__(self, identifier):
        self.identifier = identifier


class Parser(object):
    def __init__(self, lexer):
        self.lexer = lexer
        self.current_token = self.lexer.get_next_token()

    def error(self, expected):
        raise Exception(EXCEPTION_PARSE.format(
            position=self.lexer.position,
            found=self.current_token.type,
            expected=expected
        ))

    def eat(self, *token_types):
        if self.current_token.type not in token_types:
            self.error(token_types)
        self.current_token = self.lexer.get_next_token()

    def sign(self):
        if self.current_token.type == QUOTATION_MARK:
            self.eat(QUOTATION_MARK)
            node = Sign(self.current_token.value)
            self.eat(IDENTIFIER)
            self.eat(QUOTATION_MARK)
            return node

        node = Sign(self.current_token.value)
        self.eat(IDENTIFIER)
        return node

    def p_sign(self):
        if self.current_token.type == NUMBER:
            number = Number(self.current_token.value)
            self.eat(NUMBER)
            self.eat(STAR)
            sign = self.sign()
            return PSign(sign, number)

        sign = self.sign()
        return PSign(sign)

    def symbol(self):
        if self.current_token.type == BRACKET_CURLY_OPEN:
            self.eat(BRACKET_CURLY_OPEN)
            p_sign = self.p_sign()
            self.eat(BRACKET_CURLY_CLOSE)
            return Symbol(p_sign, 2)

        if self.current_token.type == BRACKET_SQUARE_OPEN:
            self.eat(BRACKET_SQUARE_OPEN)
            p_sign = self.p_sign()
            self.eat(BRACKET_SQUARE_CLOSE)
            return Symbol(p_sign, 0)

        return PSign(self.p_sign())

    def definition(self):
        definition = Definition()
        definition.symbols.append(self.symbol())

        while self.current_token.type == COMMA:
            self.eat(COMMA)
            definition.symbols.append(self.symbol())

        while self.current_token.type == ALTERNATIVE:
            self.eat(ALTERNATIVE)
            definition.definitions.append(self.definition())

        return definition

    def rule(self):
        id = self.current_token.value
        self.eat(IDENTIFIER)
        self.eat(EQUAL)
        definition = self.definition()
        self.eat(SEMICOLON)
        return Rule(id, definition)

    def program(self):
        program = Program()
        program.rules.append(self.rule())

        while self.current_token.type != EOF:
            program.rules.append(self.rule())

        return program

    def parse(self):
        return self.program()
