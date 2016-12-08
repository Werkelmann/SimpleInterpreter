IDENTIFIER = 'IDENTIFIER'

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
        while self.has_char and self.current_char.isspace():
            self.advance()

    def read_identifier(self):
        result = ''
        while self.has_char and self.current_char.isalnum():
            result += self.current_char
            self.advance()

        return Token(IDENTIFIER, result)

    def compare(self, char):
        return self.current_char == char

    def get_next_token(self):
        while self.has_char():
            if self.current_char.isspace():
                self.skip_whitespace()

            if self.current_char.isalpha():
                return self.read_identifier()

            if self.compare('|'):
                self.advance()
                return Token(ALTERNATIVE, ALTERNATIVE)

            if self.compare('{'):
                self.advance()
                return Token(BRACKET_CURLY_OPEN, BRACKET_CURLY_OPEN)

            if self.compare('}'):
                self.advance()
                return Token(BRACKET_CURLY_CLOSE, BRACKET_CURLY_CLOSE)

            if self.compare('['):
                self.advance()
                return Token(BRACKET_SQUARE_OPEN, BRACKET_SQUARE_OPEN)

            if self.compare(']'):
                self.advance()
                return Token(BRACKET_SQUARE_CLOSE, BRACKET_SQUARE_CLOSE)

            if self.compare(','):
                self.advance()
                return Token(COMMA, COMMA)

            if self.compare('='):
                self.advance()
                return Token(EQUAL, EQUAL)

            if self.compare('"'):
                self.advance()
                return Token(QUOTATION_MARK, QUOTATION_MARK)

            if self.compare(';'):
                self.advance()
                return Token(SEMICOLON, SEMICOLON)

            if self.compare('*'):
                self.advance()
                return Token(STAR, STAR)

            self.error()

        return Token(EOF, EOF)

