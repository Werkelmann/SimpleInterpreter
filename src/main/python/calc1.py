
INTEGER, OPERATOR, EOF, LPARENTHESIS, RPARENTHESIS = 'INTEGER', 'OPERATOR', 'EOF', 'LPARENTHESIS', 'RPARENTHESIS'
EXCEPTION_PARSE = 'Error while parsing at {position}. Found: {found}'
EXCEPTION_UNKNOWN_OPERATOR = 'Unknown operator at {}. Found: {}'


class Token(object):
    def __init__(self, type, value):
        self.type = type
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

    def error(self, message):
        raise Exception(message.format(
            position=self.position,
            found=self.current_token.value))

    def advance(self):
        self.position += 1
        if self.position > len(self.text) - 1:
            self.current_char = None
        else:
            self.current_char = self.text[self.position]

    def skip_whitespace(self):
        while self.current_char is not None and self.current_char.isspace():
            self.advance()

    def integer(self):
        result = ''
        while self.current_char is not None and self.current_char.isdigit():
            result += self.current_char
            self.advance()
        return int(result)

    def get_next_token(self):
        while self.current_char is not None:

            if self.current_char.isspace():
                self.skip_whitespace()
                continue

            if self.current_char.isdigit():
                return Token(INTEGER, self.integer())

            if self.current_char in ('+', '-', '*', '/'):
                token = Token(OPERATOR, self.current_char)
                self.advance()
                return token

            if self.current_char == '(':
                token = Token(LPARENTHESIS, self.current_char)
                self.advance()
                return token

            if self.current_char == ')':
                token = Token(RPARENTHESIS, self.current_char)
                self.advance()
                return token

            self.error(EXCEPTION_PARSE)

        return Token(EOF, None)


class Interpreter(object):
    def __init__(self, lexer):
        self.lexer = lexer
        self.current_token = self.lexer.get_next_token()

    def error(self, message):
        raise Exception(message.format(
            position=self.lexer.position,
            found=self.current_token.value))

    def eat(self, token_type):
        if self.current_token.type == token_type:
            self.current_token = self.lexer.get_next_token()
        else:
            self.error(EXCEPTION_PARSE)

    def factor(self):
        token = self.current_token
        if token.type == INTEGER:
            self.eat(INTEGER)
            return token.value

        if token.type == LPARENTHESIS:
            self.eat(LPARENTHESIS)
            result = self.expr()
            self.eat(RPARENTHESIS)
            return result

    def term(self):
        result = self.factor()
        while self.current_token.value in ('*', '/'):
            op = self.current_token
            self.eat(OPERATOR)

            if op.value == '*':
                result = result * self.term()
            if op.value == '/':
                result = result / self.term()

        if result is not None:
            return result

        self.error(EXCEPTION_UNKNOWN_OPERATOR)

    def expr(self):
        result = self.term()
        while self.current_token.value in ('+', '-'):
            op = self.current_token
            self.eat(OPERATOR)

            if op.value == '+':
                result = result + self.term()
            if op.value == '-':
                result = result - self.term()

        if result is not None:
            return result

        self.error(EXCEPTION_UNKNOWN_OPERATOR)


def main():
    while True:
        try:
            text = input('calc> ')
        except EOFError:
            break
        if not text:
            continue

        lexer = Lexer(text)
        interpreter = Interpreter(lexer)
        result = interpreter.expr()
        print(result)

if __name__ == '__main__':
    main()
