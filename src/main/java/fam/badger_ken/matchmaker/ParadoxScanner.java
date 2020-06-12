// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

/**
 * This class scans Paradox files. Neither the Java Scanner nor Tokenizer
 * classes do quite the right thing here, it turns out to be easier to just
 * write our own. The rules are:
 * -) double-quotes quote a token. They may not be escaped or nested.
 * -) Tokens are sequences of anything OTHER than (a) whitespace, (b) '{',
 * '}', or '='.
 * -) A '#', if not in double-quotes, comments out the rest of the line.
 */
public class ParadoxScanner implements Iterator<String> {
    private final BufferedReader reader_;
    // the next token to return - pre-computed.
    private StringBuilder nextToken_ = null;
    // for one-character put-back: if not null, this is the next one to read
    private Character nextChar_ = null;

    public ParadoxScanner(BufferedReader reader) {
        reader_ = reader;
        advance();
    }

    @Override
    public boolean hasNext() {
        return nextToken_ != null;
    }

    @Override
    public String next() {
        if (nextToken_ == null) return null;
        String hold = nextToken_.toString();
        advance();
        return hold;
    }

    @Override
    public void remove() {
        advance();
    }

    private enum ParserState {
        IN_QUOTE,
        IN_COMMENT,
        IN_TOKEN,
        AT_START
    }

    private int getc() {
        int val;
        if (nextChar_ != null) {
            val = nextChar_;
            nextChar_ = null;
        } else {
            try {
                val = reader_.read();
            } catch (IOException e) {
                val = -1;
            }
        }
        return val;
    }

    private void ungetc(char c) {
        assert (nextChar_ == null);
        nextChar_ = c;
    }

    /**
     * This is the main worker procedure - it looks through the input
     * reader for the next token, and puts it into (nextToken_).
     */
    private void advance() {
        if (reader_ == null) {
            nextToken_ = null;
            return;
        }
        ParserState state = ParserState.AT_START;
        nextToken_ = new StringBuilder();
        for (; ; ) {
            int code = getc();
            if (code == -1) {  // EOF
                sealToken();
                return;
            }
            char c = (char) code;
            switch (state) {
                case IN_QUOTE:
                    if (c == '"') {
                        //sealToken();  -- an empty string is just fine.
                        return;
                    }
                    nextToken_.append(c);
                    break;
                case IN_COMMENT:
                    if (c == '\n') {  // EOL ends comments
                        state = ParserState.AT_START;
                    }
                    break;
                case AT_START:
                    nextToken_ = new StringBuilder();
                    if (c == '#') {
                        state = ParserState.IN_COMMENT;
                        break;
                    } else if (c == '"') {
                        state = ParserState.IN_QUOTE;
                        break;
                    } else if (c == '{' || c == '}' || c == '=') {
                        nextToken_.append(c);
                        return;
                    } else if (Character.isWhitespace(c)) {
                        break;
                    } else {  // starting a token
                        nextToken_.append(c);
                        state = ParserState.IN_TOKEN;
                        break;
                    }
                case IN_TOKEN:
                    if (c == '#') {
                        // the current token is ending with this comment.
                        // put the comment back so we see it next time.
                        ungetc(c);
                        return;
                    } else if (c == '"') {
                        state = ParserState.IN_QUOTE;
                        break;
                    } else if (c == '{' || c == '}' || c == '=') {
                        ungetc(c);
                        return;
                    } else if (Character.isWhitespace(c)) {
                        ungetc(c);
                        return;
                    } else {
                        nextToken_.append(c);
                        break;
                    }
            } // end switch
        }  // end for
    } // end proc


    private void sealToken() {
        if (nextToken_.toString().equals("")) nextToken_ = null;
    }


}
