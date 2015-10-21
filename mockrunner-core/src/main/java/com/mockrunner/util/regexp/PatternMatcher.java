package com.mockrunner.util.regexp;

/**
 * Defines contract for matching strings.
 *
 * @author Radim Vansa &lt;rvansa@redhat.com&gt;
 */
public interface PatternMatcher extends Comparable<PatternMatcher> {
   /**
    * String representation of the type of expressions matched by this pattern matcher.
    * Examples are 'Perl5', 'JSR-51' or 'equals'. Used for comparison; see {@link #equals(Object)}.
    * @return The type of this matcher
    */
   String type();

   /**
    * @return Original pattern used for creation of this instance.
    */
   String pattern();

   /**
    * @param string
    * @return True if the string matches to this pattern.
    */
   boolean matches(String string);

   /**
    * @return The same value as <code>{@link #pattern()}.hashCode()</code>
    */
   int hashCode();

   /**
    * Two instances are considered equal if these use the same {@link #type()} and {@link #pattern()}.
    *
    * @param other
    * @return
    */
   boolean equals(Object other);


   interface Factory {
      PatternMatcher create(String pattern);
   }

   class Factories {
      public static Factory from(boolean caseSensitive, boolean exactMatch, boolean useRegularExpressions) {
         if (exactMatch) {
            return new SimplePatternMatcher.Factory(caseSensitive, true);
         } else if (useRegularExpressions) {
            return new Perl5PatternMatcher.Factory(caseSensitive);
         } else {
            return new SimplePatternMatcher.Factory(caseSensitive, exactMatch);
         }
      }
   }

   abstract class Base implements PatternMatcher {
      protected final String originalPattern;

      public Base(String pattern) {
         this.originalPattern = pattern;
      }

      public int compareTo(PatternMatcher o) {
         return pattern().compareTo(o.pattern());
      }

      @Override
      public int hashCode() {
         return pattern().hashCode();
      }

      @Override
      public boolean equals(Object obj) {
         if (obj == null) return false;
         if (obj == this) return true;
         if (obj instanceof PatternMatcher) {
            PatternMatcher other = (PatternMatcher) obj;
            return pattern().equals(other.pattern()) && type().equals(other.type());
         }
         return false;
      }

      public String pattern() {
         return originalPattern;
      }
   }
}
