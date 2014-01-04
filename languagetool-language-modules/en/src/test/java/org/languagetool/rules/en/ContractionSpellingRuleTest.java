/* LanguageTool, a natural language style checker
 * Copyright (C) 2005 Daniel Naber (http://www.danielnaber.de)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */

package org.languagetool.rules.en;

import java.io.IOException;

import junit.framework.TestCase;

import org.languagetool.JLanguageTool;
import org.languagetool.TestTools;
import org.languagetool.language.English;
import org.languagetool.rules.RuleMatch;

public class ContractionSpellingRuleTest extends TestCase {

  private ContractionSpellingRule rule;
  private JLanguageTool langTool;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    rule = new ContractionSpellingRule(TestTools.getMessages("en"));
    langTool = new JLanguageTool(new English());
  }

  public void testRule() throws IOException {

    // correct sentences:
    assertEquals(0, rule.match(langTool.getAnalyzedSentence("It wasn't me.")).length);

    // incorrect sentences:

    // at the beginning of a sentence
    checkSimpleReplaceRule("Wasnt this great", "Wasn't");
    checkSimpleReplaceRule("YOURE WRONG", "YOU'RE");
    checkSimpleReplaceRule("Dont do this", "Don't");
    // inside sentence
    checkSimpleReplaceRule("It wasnt me", "wasn't");
    checkSimpleReplaceRule("You neednt do this", "needn't");
    checkSimpleReplaceRule("I know Im wrong", "I'm");

    //two suggestions
    final RuleMatch[] matches = rule.match(langTool.getAnalyzedSentence("Whereve you are"));
    assertEquals(2, matches[0].getSuggestedReplacements().size());
    assertEquals("Where've", matches[0].getSuggestedReplacements().get(0));
    assertEquals("Wherever", matches[0].getSuggestedReplacements().get(1));

  }

  /**
   * Check if a specific replace rule applies.
   * @param sentence the sentence containing the incorrect/misspelled word.
   * @param word the word that is correct (the suggested replacement).
   * @throws IOException
   */
  private void checkSimpleReplaceRule(String sentence, String word) throws IOException {
    final RuleMatch[] matches = rule.match(langTool.getAnalyzedSentence(sentence));
    assertEquals("Invalid matches.length while checking sentence: "
        + sentence, 1, matches.length);
    assertEquals("Invalid replacement count wile checking sentence: "
        + sentence, 1, matches[0].getSuggestedReplacements().size());
    assertEquals("Invalid suggested replacement while checking sentence: "
        + sentence, word, matches[0].getSuggestedReplacements().get(0));
  }
}