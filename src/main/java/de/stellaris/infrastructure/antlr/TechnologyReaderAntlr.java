package de.stellaris.infrastructure.antlr;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import de.stellaris.domain.technology.ReaderException;
import de.stellaris.domain.technology.Technology;
import de.stellaris.domain.technology.TechnologyReader;

public class TechnologyReaderAntlr implements TechnologyReader {

  private final TechnologyAssembler assembler = new TechnologyAssembler();

  @Override
  public List<Technology> readTechnologies(Path file) throws ReaderException {

    assembler.clear();
    parse(file, assembler);
    return assembler.getTechnologies();
  }

  private void parse(Path file, TechnologyAssembler assembler) throws ReaderException {

    try {
      TechnologyLexer lexer = new TechnologyLexer(CharStreams.fromPath(file));
      CommonTokenStream tokenStream = new CommonTokenStream(lexer);
      TechnologyParser parser = new TechnologyParser(tokenStream);
      parser.addParseListener(assembler);
      parser.technologies();
    } catch (IOException e) {
      throw new ReaderException(String.format("Could not parse given file: %s", file));
    }

  }
}
