package com.mockrunner.util.regexp;

/**
 * // TODO: Document this
 *
 * @author Radim Vansa &lt;rvansa@redhat.com&gt;
 */
public class SimplePatternMatcher extends PatternMatcher.Base {
   private final String pattern;
   private final boolean caseSensitive;
   private final boolean exactMatch;

   public SimplePatternMatcher(String pattern, boolean caseSensitive, boolean exactMatch) {
      super(pattern);
      this.pattern = exactMatch || caseSensitive ? pattern : pattern.toUpperCase();
      this.caseSensitive = caseSensitive;
      this.exactMatch = exactMatch;
   }

   public String type() {
      return "equals";
   }

   public boolean matches(String string) {
      if (exactMatch) {
         if (caseSensitive) {
            return pattern.equals(string);
         } else {
            return pattern.equalsIgnoreCase(string);
         }
      } else {
         if (!caseSensitive) {
            string = string.toUpperCase();
         }
         return string.indexOf(pattern) >= 0;
      }
   }

   public static class Factory implements PatternMatcher.Factory {
      private final boolean caseSensitive;
      private final boolean exactMatch;

      public Factory(boolean caseSensitive, boolean exactMatch) {
         this.caseSensitive = caseSensitive;
         this.exactMatch = exactMatch;
      }

      public PatternMatcher create(String pattern) {
         return new SimplePatternMatcher(pattern, caseSensitive, exactMatch);
      }
   }
}
