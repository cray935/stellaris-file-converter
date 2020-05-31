package de.stellaris.infrastructure.antlr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.stellaris.domain.technology.Technology;
import de.stellaris.infrastructure.antlr.TechnologyParser.Ai_update_typeContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.Ai_weightContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.AreaContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.CategoryContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.CostContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.Cost_per_levelContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.Feature_flagsContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.GatewayContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.Is_dangerousContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.Is_rareContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.LevelsContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.Mod_weight_if_group_pickedContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.ModifierContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.PotentialContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.Prereqfor_descContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.PrerequisitesContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.Start_techContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.TechnologiesContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.TechnologyContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.TierContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.Variable_declarationContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.WeightContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.Weight_groupsContext;
import de.stellaris.infrastructure.antlr.TechnologyParser.Weight_modifierContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class TechnologyAssembler extends TechnologyBaseListener {

  private static final String YES = "yes";

  private Map<String, Double> variables = new HashMap<>();
  private List<Technology> technologies = new ArrayList<>();
  @Getter(AccessLevel.NONE)
  private Technology.TechnologyBuilder current = null;

  public void clear() {
    technologies = new ArrayList<>();
    current = null;
  }

  @Override
  public void enterTechnologies(TechnologiesContext ctx) {
    technologies.clear();
    log.debug("Started assembly");
  }

  @Override
  public void exitTechnologies(TechnologiesContext ctx) {
    log.debug("Finished assembly");
  }

  @Override
  public void enterTechnology(TechnologyContext ctx) {
    current = Technology.builder();
    log.debug("Started tech assembly");
  }

  @Override
  public void exitTechnology(TechnologyContext ctx) {
    current.id(ctx.getChild(0).getText());
    technologies.add(current.build());
    log.debug("Finished tech assembly");
  }

  @Override
  public void exitVariable_declaration(Variable_declarationContext ctx) {
    String key = ctx.getChild(1).getText();
    Double value = Double.parseDouble(ctx.getChild(3).getText());
    variables.put(key, value);
    log.debug("Variable {} = {} has been added", key, value);
  }

  @Override
  public void exitCost(CostContext ctx) {
    current.cost(parseNumberOrVariable(ctx));
  }

  @Override
  public void exitArea(AreaContext ctx) {
    current.area(ctx.getChild(2).getText());
  }

  @Override
  public void exitTier(TierContext ctx) {
    current.tier(parseNumberOrVariable(ctx));
  }

  @Override
  public void exitCategory(CategoryContext ctx) {
    current.category(ctx.getChild(3).getText());
  }

  @Override
  public void exitLevels(LevelsContext ctx) {

    if (ctx.getChildCount() == 4) {
      double levels = -Double.parseDouble(ctx.getChild(3).getText());
      current.levels(levels);
    } else {
      double levels = Double.parseDouble(ctx.getChild(2).getText());
      current.levels(levels);
    }
  }

  @Override
  public void exitCost_per_level(Cost_per_levelContext ctx) {
    current.costPerLevel(parseNumberOrVariable(ctx));
  }

  @Override
  public void exitPrerequisites(PrerequisitesContext ctx) {
    List<TerminalNode> tokens = ctx.getTokens(TechnologyLexer.WORD);
    for (TerminalNode token : tokens) {
      current.prerequisite(token.getText());
    }
  }

  @Override
  public void exitWeight(WeightContext ctx) {
    current.weight(parseNumberOrVariable(ctx));
  }

  @Override
  public void exitGateway(GatewayContext ctx) {
    current.gateway(ctx.getChild(2).getText());
  }

  @Override
  public void exitAi_update_type(Ai_update_typeContext ctx) {
    current.aiUpdateType(ctx.getChild(2).getText());
  }

  @Override
  public void exitStart_tech(Start_techContext ctx) {
    boolean startTech = YES.equals(ctx.getChild(2).getText());
    current.startTech(startTech);
  }

  @Override
  public void exitIs_dangerous(Is_dangerousContext ctx) {
    boolean dangerous = YES.equals(ctx.getChild(2).getText());
    current.dangerous(dangerous);
  }

  @Override
  public void exitIs_rare(Is_rareContext ctx) {
    boolean rare = YES.equals(ctx.getChild(2).getText());
    current.rare(rare);
  }

  @Override
  public void exitFeature_flags(Feature_flagsContext ctx) {
    ctx.getTokens(TechnologyParser.WORD).stream()
        .map(TerminalNode::getText)
        .forEach(current::featureFlag);
  }

  @Override
  public void exitModifier(ModifierContext ctx) {
    log.debug("Found modifier but skipped: {}", ctx.getText());
  }

  @Override
  public void exitPrereqfor_desc(Prereqfor_descContext ctx) {
    log.debug("Found prereqfor_desc but skipped: {}", ctx.getText());
  }

  @Override
  public void exitPotential(PotentialContext ctx) {
    log.debug("Found potential but skipped: {}", ctx.getText());
  }

  @Override
  public void exitWeight_modifier(Weight_modifierContext ctx) {
    log.debug("Found weight_modifier but skipped: {}", ctx.getText());
  }

  @Override
  public void exitWeight_groups(Weight_groupsContext ctx) {
    log.debug("Found weight_groups but skipped: {}", ctx.getText());
  }

  @Override
  public void exitMod_weight_if_group_picked(Mod_weight_if_group_pickedContext ctx) {
    log.debug("Found mod_weight_if_group_picked but skipped: {}", ctx.getText());
  }

  @Override
  public void exitAi_weight(Ai_weightContext ctx) {
    log.debug("Found ai_weight but skipped: {}", ctx.getText());
  }

  @Override
  public void exitEveryRule(ParserRuleContext ctx) {
    log.debug(ctx.getText());
  }

  private double parseNumberOrVariable(ParserRuleContext ctx) {

    double value = 0;
    if (ctx.getChildCount() == 4) {
      String variable = ctx.getChild(3).getText();
      if (variables.get(variable) != null) {
        value = variables.get(variable);
      } else {
        log.debug("Unknown variable: {}", variable);
      }
    } else {
      value = Double.parseDouble(ctx.getChild(2).getText());
    }
    return value;
  }
}
