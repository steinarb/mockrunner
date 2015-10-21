package com.mockrunner.util.regexp;

import java.util.regex.Pattern;

/**
 * @author Radim Vansa &lt;rvansa@redhat.com&gt;
 */
public class Jsr51PatternMatcher extends PatternMatcher.Base {
   private final Pattern pattern;

   public Jsr51PatternMatcher(String pattern, boolean caseSensitive) {
      super(pattern);
      this.pattern = Pattern.compile(pattern, caseSensitive ? 0 : Pattern.CASE_INSENSITIVE);
   }

   public String type() {
      return "JSR-51";
   }

   public boolean matches(String string) {
      return pattern.matcher(string).matches();
   }

   public static class Factory implements PatternMatcher.Factory {
      private final boolean caseSensitive;

      public Factory(boolean caseSensitive) {
         this.caseSensitive = caseSensitive;
      }

      public PatternMatcher create(String pattern) {
         return new Jsr51PatternMatcher(pattern, caseSensitive);
      }
   }
}
