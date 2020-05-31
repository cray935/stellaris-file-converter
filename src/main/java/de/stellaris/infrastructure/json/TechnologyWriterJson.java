package de.stellaris.infrastructure.json;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stellaris.domain.technology.Technology;
import de.stellaris.domain.technology.TechnologyWriter;
import de.stellaris.domain.technology.WriterException;

public class TechnologyWriterJson implements TechnologyWriter {

  @Override
  public void writeTechnologies(Path output, List<Technology> technologies) throws WriterException {

    ObjectMapper mapper = new ObjectMapper();
    try {
      writeJsonArray(output, mapper, technologies);
    } catch (IOException e) {
      throw new WriterException("Could not write technologies", e);
    }
  }

  private void writeJsonArray(Path output, ObjectMapper mapper, List<Technology> technologies) throws IOException {

    try (Writer out = new FileWriter(output.toFile())) {
      mapper.writer()
          .forType(Technology.class)
          .withDefaultPrettyPrinter()
          .writeValuesAsArray(out)
          .writeAll(technologies);
    }
  }
}
