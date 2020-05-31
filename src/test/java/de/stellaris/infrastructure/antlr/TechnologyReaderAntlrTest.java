package de.stellaris.infrastructure.antlr;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.stellaris.domain.technology.Technology;
import de.stellaris.domain.technology.TechnologyReader;

public class TechnologyReaderAntlrTest {

  TechnologyReader service = new TechnologyReaderAntlr();

  @Test
  public void test() throws Exception {

    Technology expected = Technology.builder()
        .aiUpdateType("military")
        .category("biology")
        .area("society")
        .cost(500)
        .costPerLevel(0) // TODO: use pre defined variables or parsed variables from other files
        .featureFlag("my_tech_flag")
        .gateway("biological")
        .id("technology_name")
        .levels(-1)
        .prerequisite("prerequisite_technology")
        .startTech(true)
        .tier(1)
        .weight(100)
        .dangerous(true)
        .rare(true)
        .build();

    Path file = Paths.get(getClass().getResource("/technology/Template.txt").toURI());
    List<Technology> actual = service.readTechnologies(file);

    assertThat(actual, hasSize(1));
    Technology tech = actual.get(0);
    assertEquals(expected.getAiUpdateType(), tech.getAiUpdateType());
    assertEquals(expected.getCategory(), tech.getCategory());
    assertEquals(expected.getArea(), tech.getArea());
    assertEquals(expected.getCost(), tech.getCost());
    assertEquals(expected.getCostPerLevel(), tech.getCostPerLevel());
    assertEquals(expected.getFeatureFlags(), tech.getFeatureFlags());
    assertEquals(expected.getGateway(), tech.getGateway());
    assertEquals(expected.getId(), tech.getId());
    assertEquals(expected.getLevels(), tech.getLevels());
    assertEquals(expected.getPrerequisites(), tech.getPrerequisites());
    assertEquals(expected.isStartTech(), tech.isStartTech());
    assertEquals(expected.getTier(), tech.getTier());
    assertEquals(expected.getWeight(), tech.getWeight());
    assertEquals(expected.isDangerous(), tech.isDangerous());
    assertEquals(expected.isRare(), tech.isRare());
  }
}
