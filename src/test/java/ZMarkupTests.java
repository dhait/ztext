
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.Test;
import org.optionmetrics.ztext.ZMarkupLexer;
import org.optionmetrics.ztext.ZMarkupParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ZMarkupTests {

    public String getResourceAsString(String name) throws IOException {
        InputStream input = this.getClass().getResourceAsStream(name);
        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        for (int c = br.read(); c != -1; c = br.read())
            sb.append((char)c);
        return sb.toString();
    }
    @Test
    public void basicTest() throws IOException {

        ThrowingErrorListener errorListener = new ThrowingErrorListener();

        String text = getResourceAsString("/birthday.ztx");
        CharStream stream = CharStreams.fromString(text);
        ZMarkupLexer lexer = new ZMarkupLexer(stream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ZMarkupParser parser = new ZMarkupParser(tokens);

        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        ParserRuleContext tree = parser.specification();
        // No exception means successful parse
   }

}
