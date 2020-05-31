package de.stellaris;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import de.stellaris.domain.technology.ReaderException;
import de.stellaris.domain.technology.Technology;
import de.stellaris.domain.technology.TechnologyFilters;
import de.stellaris.domain.technology.TechnologyReader;
import de.stellaris.domain.technology.TechnologyWriter;
import de.stellaris.domain.technology.WriterException;
import de.stellaris.infrastructure.antlr.TechnologyReaderAntlr;
import de.stellaris.infrastructure.json.TechnologyWriterJson;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleApp {

  private static final String DATA_ROOT = "stellaris_data";
  private static final String INPUT_DIR = "raw_tech";
  private static final String OUTPUT_DIR = "json_tech";
  private static final TechnologyReader READER_SERVICE = new TechnologyReaderAntlr();
  private static final TechnologyWriter WRITER_SERVICE = new TechnologyWriterJson();
  private static final Predicate<Technology> DEFAULT_LIST_FILTER = TechnologyFilters.defaultList();
  private static final String DEFAULT_LIST_FILE = "usual_tech.json";

  public static void main(String[] args) throws Exception {

    Path input = Paths.get(System.getProperty("user.home"), DATA_ROOT, INPUT_DIR);
    Path output = Paths.get(System.getProperty("user.home"), DATA_ROOT, OUTPUT_DIR);

    List<Technology> defaultList = new ArrayList<>();

    Files.list(input).forEach(txt -> {
      String fileName = txt.getFileName().toString();
      log.info("Processing file: {}", fileName);
      try {
        List<Technology> techs = READER_SERVICE.readTechnologies(txt);
        techs.stream().filter(DEFAULT_LIST_FILTER).forEach(defaultList::add);
        Path json = Path.of(output.toString(), fileName.replaceAll("\\.txt", ".json"));
        WRITER_SERVICE.writeTechnologies(json, techs);
      } catch (ReaderException | WriterException e) {
        log.error("Failed to convert file: {}", txt, e);
      }
    });

    WRITER_SERVICE.writeTechnologies(Paths.get(output.toString(), DEFAULT_LIST_FILE), defaultList);
  }
}
