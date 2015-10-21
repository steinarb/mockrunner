package com.mockrunner.util.regexp;

/**
 * Simplified comparison based on JSR-51 regular expressions, applicable only to
 * patterns with single .* part.
 *
 * @author Radim Vansa &lt;rvansa@redhat.com&gt;
 */
public class StartsEndsPatternMatcher extends PatternMatcher.Base {
   private final String start;
   private final String end;
   private final int minLength;

   public StartsEndsPatternMatcher(String pattern, String start, String end) {
      super(pattern);
      this.start = start;
      this.end = end;
      this.minLength = (start == null ? 0 : start.length()) + (end == null ? 0 : end.length());
   }

   public String type() {
      return "JSR-51";
   }

   public boolean matches(String string) {
      if (string.length() < minLength) return false;
      if (start != null && !string.startsWith(start)) return false;
      if (end != null && !string.endsWith(end)) return false;
      return true;
   }

   public static class Factory implements PatternMatcher.Factory {
      public PatternMatcher create(final String pattern) {
         String p = pattern;
         // these are considered implicitly
         if (p.startsWith("^")) p = p.substring(1);
         if (p.endsWith("$")) p = p.substring(0, p.length() - 1);
         int dotStar = p.indexOf(".*");

         if (dotStar < 0) {
            String unescaped = unescape(p);
            if (unescaped == null) {
               return new Jsr51PatternMatcher(pattern, true);
            } else {
               return new StartsEndsPatternMatcher(pattern, unescaped, null);
            }
         } else {
            int secondDotStar = p.indexOf(".*", dotStar + 2);
            if (secondDotStar < 0) {
               String unescapedStart = unescape(p.substring(0, dotStar));
               String unescapedEnd = unescape(p.substring(dotStar + 2));
               if (unescapedStart != null && unescapedEnd != null) {
                  return new StartsEndsPatternMatcher(pattern,
                        unescapedStart.length() == 0 ? null : unescapedStart,
                        unescapedEnd.length() == 0 ? null : unescapedEnd);
               }
            }
            return new Jsr51PatternMatcher(pattern, true);
         }
      }

      private String unescape(String pattern) {
         StringBuilder sb = new StringBuilder(pattern.length());
         boolean escaped = false;
         for (char c : pattern.toCharArray()) {
            if (isSpecial(c)) {
               if (escaped) {
                  sb.append(c);
                  escaped = false;
               } else if (c == '\\') {
                  escaped = true;
               } else {
                  // special character not escaped
                  return null;
               }
            } else if (escaped) {
               // unrecognized escape sequence
               return null;
            } else {
               sb.append(c);
               escaped = false;
            }
         }
         return sb.toString();
      }

      private boolean isSpecial(char c) {
         switch (c) {
            case '\\':
            case '?':
            case '.':
            case '[':
            case ']':
            case '{':
            case '}':
               return true;
            default:
               return false;
         }
      }
   }

}
