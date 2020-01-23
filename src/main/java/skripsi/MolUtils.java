package skripsi;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.marvin.Sketch;
import chemaxon.marvin.calculations.ConformerPlugin;
import chemaxon.marvin.modelling.struc.ConformerEquivalenceUtils;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.struc.Molecule;

import java.io.*;
import java.util.logging.Logger;

public class MolUtils {

    private MolUtils() {}

    private static Logger logger = Logger.getLogger(MolUtils.class.getName());

    public static Molecule smilesToMolecule(String smiles) throws IOException {
        InputStream is = new ByteArrayInputStream(smiles.getBytes());
        MolImporter importer = new MolImporter(is, "smiles");
        return importer.read();
    }

    public static ConformerPlugin optimizeMolecule(Smiles smiles) throws PluginException, IOException {
        // create plugin
        // you need to run the marvin sketch GUI in other thread
        // and you need the license to use this feature
        ConformerPlugin plugin = new ConformerPlugin();

        // set target molecule
        plugin.setMolecule(smiles.getMolecule());

        // set parameters for calculation
        plugin.setMaxNumberOfConformers(10);
        plugin.setMMFF94(true);
        plugin.setOptimization(ConformerPlugin.OPTIMIZATION_STRICT);
        plugin.setTimelimit(900);
        // run the calculatioDn
        plugin.run();

        // get results
        return plugin;
    }

    public static void exportAsFile(Molecule molecule, String format, String filename) {
        // open I/O
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(String.format("export/%s", filename)))) {
            // export molecule to expected format
            String exportedMolecule = MolExporter.exportToFormat(molecule, format);
            // write molecule to file
            bw.write(exportedMolecule);
        } catch (Exception e) {
            logger.severe(String
                    .format("Failed to export molecule %s to %s format | error: %s",
                            molecule.toString(), format, e.getMessage()));
        }
    }
}
