
INTEGER, OPERATOR, EOF = "INTEGER", "OPERATOR", "EOF"


class Token(object):
    def __init__(self, type, value):
        self.type = type
        self.value = value

    def __str__(self):
        return 'Token({type}: {value})'.format(
            type = self.type,
            value = repr(self.value)
        )

    def __repr__(self):
        return self.__str__()


class Interpreter(object):
    def __init__(self, text):
        self.text = text
        self.position = 0
        self.currentToken = None
        self.current_char = self.text[self.position]

    def error(self):
        raise Exception('Error parsing input')

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

            if self.current_char == '+' or self.current_char == '-':
                token = Token(OPERATOR, self.current_char)
                self.advance()
                return token

            self.error()

        return Token(EOF, None)

    def eat(self, token_type):
        if self.currentToken.type == token_type:
            self.currentToken = self.get_next_token()
        else:
            self.error()

    def expr(self):
        self.currentToken = self.get_next_token()

        left = self.currentToken
        self.eat(INTEGER)

        op = self.currentToken
        self.eat(OPERATOR)

        right = self.currentToken
        self.eat(INTEGER)

        if op.value == '+':
            result = left.value + right.value

        if op.value == '-':
            result = left.value - right.value

        return result

def main():
    while True:
        try:
            text = input('calc> ')
        except EOFError:
            break
        if not text:
            continue
        interpreter = Interpreter(text)
        result = interpreter.expr()
        print(result)

if __name__ == '__main__':
    main()