package skripsi;

import chemaxon.struc.Molecule;

import java.io.IOException;

public class Smiles {

    private String id;
    private String name;
    private String value;
    private String compoundClass;
    private Molecule molecule;

    public Smiles(String id, String name, String value, String compoundClass) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.compoundClass = compoundClass;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getCompoundClass() {
        return compoundClass;
    }

    public Molecule getMolecule() throws IOException {
        if(molecule == null) {
            molecule = MolUtils.smilesToMolecule(getValue());
        }
        return molecule;
    }

    public final String getFileName(String fileFormat) {
        return String.format("%s - %s - %s [%s]", getId(), getCompoundClass(), getName(), fileFormat);
    }

    @Override
    public String toString() {
        return getValue();
    }
}
