package com.mockrunner.util.regexp;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * @author Radim Vansa &lt;rvansa@redhat.com&gt;
 */
public class Perl5PatternMatcher extends PatternMatcher.Base {
   private final Pattern pattern;

   public Perl5PatternMatcher(String pattern, boolean caseSensitive) {
      super(pattern);
      try {
         this.pattern = new Perl5Compiler().compile(pattern,
               caseSensitive ? Perl5Compiler.DEFAULT_MASK : Perl5Compiler.CASE_INSENSITIVE_MASK);
      } catch (MalformedPatternException e) {
         throw new IllegalArgumentException(e);
      }
   }

   public String type() {
      return "Perl5";
   }

   public boolean matches(String string) {
      return (new Perl5Matcher().matches(string, pattern));
   }

   public static class Factory implements PatternMatcher.Factory {
      private final boolean caseSensitive;

      public Factory(boolean caseSensitive) {
         this.caseSensitive = caseSensitive;
      }

      public PatternMatcher create(String pattern) {
         return new Perl5PatternMatcher(pattern, caseSensitive);
      }
   }
}
