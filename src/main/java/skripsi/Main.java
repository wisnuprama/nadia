package skripsi;

import chemaxon.marvin.Sketch;
import chemaxon.marvin.calculations.ConformerPlugin;
import chemaxon.marvin.plugin.PluginException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());

    // magic number, it took 5 seconds in mind
    private static final int TIMEOUT_AFTER_SKETCH_WINDOW_OPENED = 5;

    // the targeted file format
    private static final String FILE_FORMAT_PDB = "pdb";
    private static final String FILE_FORMAT_MOL2 = "mol2";

    public static void main(String[] args) throws Exception {
        // load smiles
        List<Smiles> smilesList = CsvUtils.csvToSmiles(CsvUtils.getCompundCsvFile());
        logger.info(String.format("%d molecules loaded", smilesList.size()));

        // the conformer plugin can run when there is a sketch running
        Thread thread = new Thread(() -> Sketch.main(args));
        thread.start();

        // wait till the window fully open and run the calculation
        TimeUnit.SECONDS.sleep(TIMEOUT_AFTER_SKETCH_WINDOW_OPENED);

        // run the optimization and converting to other format (mol2, pdb)
        smilesList.forEach(Main::optimize);
        smilesList.forEach(Main::export);
    }

    public static void optimize(Smiles smiles) {
        try {
            logger.info(String.format("-- STARTING ID:%s", smiles.getId()));

            // optimize all molecule in conformer
            logger.info(String.format("-- CALCULATING AND OPTIMIZING ID:%s ...", smiles.getId()));
            ConformerPlugin conformerPlugin = MolUtils.optimizeMolecule(smiles);
            logger.info(String.format("-- ID:%s has been optimized", smiles.getId()));

            // nadia wants the first picture, so for now I assume that
            // the results of the conformer reflecting the result on the window
            // Nadia see while she manually converting them.
            final int TARGET_INDEX = 0;
            // i don't like this design because mutating the object, but fine for now
            smiles.setMolecule(conformerPlugin.getConformer(TARGET_INDEX));
        } catch (IOException | PluginException e) {
            logger.severe(String.format("Failed to optimize ID:%s | error: %s", smiles.getId(), e.getMessage()));
        }
    }

    public static void export(Smiles smiles) {
        try {
            // export to mol2
            logger.info(String.format("-- EXPORTING ID:%s to MOL2 format...", smiles.getId()));
            MolUtils.exportAsFile(smiles.getMolecule(), FILE_FORMAT_MOL2, smiles.getFileName(FILE_FORMAT_MOL2));
            logger.info(String.format("-- EXPORTED ID:%s to MOL2 format", smiles.getId()));

            // export to pdb
            logger.info(String.format("-- EXPORTING ID:%s to PDB format...", smiles.getId()));
            MolUtils.exportAsFile(smiles.getMolecule(), FILE_FORMAT_PDB, smiles.getFileName(FILE_FORMAT_PDB));
            logger.info(String.format("-- EXPORTED ID:%s to PDB format", smiles.getId()));

            logger.info(String.format("-- FINISHED ID:%s", smiles.getId()));
        } catch (IOException e) {
            logger.info(String.format("-- FAILED TO EXPORT ID:%s", smiles.getId()));
        }
    }
}
