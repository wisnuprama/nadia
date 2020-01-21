package skripsi;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CsvUtils {

    private static Logger logger = Logger.getLogger(CsvUtils.class.getName());

    private CsvUtils() {}

    public static final int MAX_SMILES = 350;

    public static Reader getCompundCsvFile() throws URISyntaxException, IOException {
        return Files.newBufferedReader(Paths.get(
                ClassLoader.getSystemResource("compounds.csv").toURI()));
    }

    public static List<Smiles> csvToSmiles(Reader reader) throws IOException {
        // create customer parser with separator ',' and ignore quotation
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();
        // build the reader and skip the header
        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();
        // initiate the array with max smiles
        List<Smiles> list = new ArrayList<>(MAX_SMILES);
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            // read every line and create the smile object based on the csv
            // below is the index of the header:
            // 0: id, 1: smiles value, 2: compound_class, 4: name
            try {
                Smiles newSmiles = new Smiles(line[0], line[4], line[1], line[2]);
                list.add(newSmiles);
            } catch (IndexOutOfBoundsException e) {
                logger.severe(e.getMessage());
            }
        }

        reader.close();
        csvReader.close();

        return list;
    }
}
