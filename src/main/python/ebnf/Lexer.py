from enum import Enum

IDENTIFIER = 'IDENTIFIER'
NUMBER = 'NUMBER'

# RESERVED SIGNS
ALTERNATIVE = 'ALTERNATIVE'                     # |
BRACKET_CURLY_OPEN = 'BRACKET_CURLY_OPEN'       # {
BRACKET_CURLY_CLOSE = 'BRACKET_CURLY_CLOSE'     # }
BRACKET_SQUARE_OPEN = 'BRACKET_SQUARE_OPEN'     # [
BRACKET_SQUARE_CLOSE = 'BRACKET_SQUARE_CLOSE'   # ]
COMMA = 'COMMA'                                 # ,
EQUAL = 'EQUAL'                                 # =
QUOTATION_MARK = 'QUOTATION_MARK'               # "
SEMICOLON = 'SEMICOLON'                         # ;
STAR = 'STAR'                                   # *

# OTHER
EOF = 'EOF'
EXCEPTION_SCANNING = 'Error while scanning at {position}. Found: {found}'


class State(Enum):
    IS_READING_TERMINAL = 'IS_READING_TERMINAL'
    WAIT_FOR_SECOND_QUOTATION_MARK = 'WAIT_FOR_SECOND_QUOTATION_MARK'
    WAIT_FOR_QUOTATION_MARK = 'WAIT_FOR_QUOTATION_MARK'


class Token(object):
    def __init__(self, token_type, value):
        self.type = token_type
        self.value = value

    def __str__(self):
        return 'Token({type}: {value})'.format(
            type=self.type,
            value=repr(self.value)
        )

    def __repr__(self):
        return self.__str__()


class Lexer(object):
    def __init__(self, text):
        self.text = text
        self.position = 0
        self.current_token = None
        self.current_char = self.text[self.position]
        self.state = State.WAIT_FOR_QUOTATION_MARK

    def error(self):
        raise Exception(EXCEPTION_SCANNING.format(
            position=self.position,
            found=self.current_token.value))

    def advance(self):
        self.position += 1
        if self.position > len(self.text) - 1:
            self.current_char = None
        else:
            self.current_char = self.text[self.position]

    def has_char(self):
        return self.current_char is not None

    def skip_whitespace(self):
        while self.has_char() and self.current_char.isspace():
            self.advance()

    def skip_comment(self):
        while self.has_char() and self.current_char != '#':
            self.advance()
        self.advance()

    def read_terminal(self):
        result = ''
        while self.has_char() and self.current_char != '"':
            result += self.current_char
            self.advance()

        self.state = State.WAIT_FOR_SECOND_QUOTATION_MARK
        return self.return_token(Token(IDENTIFIER, result))

    def read_identifier(self):
        result = ''
        while self.has_char() and self.current_char.isalnum():
            result += self.current_char
            self.advance()

        return self.return_token(Token(IDENTIFIER, result))

    def read_number(self):
        result = ''
        while self.has_char() and self.current_char.isdigit():
            result += self.current_char
            self.advance()

        return self.return_token(Token(NUMBER, int(result)))

    def compare(self, char):
        return self.current_char == char

    def return_token(self, token):
        self.current_token = token
        return token

    def get_next_token(self):
        while self.has_char():
            self.skip_whitespace()
            if not self.has_char():
                break

            if self.compare('#'):
                self.advance()
                self.skip_comment()
                continue

            if self.state == State.IS_READING_TERMINAL:
                return self.read_terminal()

            if self.current_char.isalpha():
                return self.read_identifier()

            if self.current_char.isdigit():
                return self.read_number()

            if self.compare('|'):
                self.advance()
                return self.return_token(Token(ALTERNATIVE, ALTERNATIVE))

            if self.compare('{'):
                self.advance()
                return self.return_token(Token(BRACKET_CURLY_OPEN, BRACKET_CURLY_OPEN))

            if self.compare('}'):
                self.advance()
                return self.return_token(Token(BRACKET_CURLY_CLOSE, BRACKET_CURLY_CLOSE))

            if self.compare('['):
                self.advance()
                return self.return_token(Token(BRACKET_SQUARE_OPEN, BRACKET_SQUARE_OPEN))

            if self.compare(']'):
                self.advance()
                return self.return_token(Token(BRACKET_SQUARE_CLOSE, BRACKET_SQUARE_CLOSE))

            if self.compare(','):
                self.advance()
                return self.return_token(Token(COMMA, COMMA))

            if self.compare('='):
                self.advance()
                return self.return_token(Token(EQUAL, EQUAL))

            if self.compare('"'):
                self.advance()
                self.switch_state()
                return self.return_token(Token(QUOTATION_MARK, QUOTATION_MARK))

            if self.compare(';'):
                self.advance()
                return self.return_token(Token(SEMICOLON, SEMICOLON))

            if self.compare('*'):
                self.advance()
                return self.return_token(Token(STAR, STAR))

            self.error()

        return self.return_token(Token(EOF, EOF))

    def switch_state(self):
        if self.state == State.WAIT_FOR_QUOTATION_MARK:
            self.state = State.IS_READING_TERMINAL
        elif self.state == State.WAIT_FOR_SECOND_QUOTATION_MARK:
            self.state = State.WAIT_FOR_QUOTATION_MARK
        elif self.state == State.IS_READING_TERMINAL:
            self.error()
