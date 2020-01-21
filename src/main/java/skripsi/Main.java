package skripsi;

import chemaxon.marvin.Sketch;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.struc.Molecule;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {
        // load smiles
        List<Smiles> smilesList = CsvUtils.csvToSmiles(CsvUtils.getCompundCsvFile());
        logger.info(String.format("%d molecules loaded", smilesList.size()));

        smilesList.forEach(smiles -> {
            try {
                // optimize all molecule in conformer
                Molecule[] optimizedMols = MolUtils.optimizeMolecule(smiles);

                // nadia wants the first picture
                final int TARGET_INDEX = 0;
                Molecule targetMol = optimizedMols[TARGET_INDEX];

                // export to mol2
                MolUtils.exportAsFile(targetMol, "mol2", smiles.getFileName("mol2"));
                // export to pdb
                MolUtils.exportAsFile(targetMol, "pdb", smiles.getFileName("pdb"));
            } catch (IOException | PluginException e) {
                e.printStackTrace();
            }
        });
    }
}
