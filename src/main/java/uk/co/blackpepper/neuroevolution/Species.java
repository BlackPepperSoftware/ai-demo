package uk.co.blackpepper.neuroevolution;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Species {

    private List<Genome> genomes;
    private Genome representative;

    public Species() {
        this.genomes = new ArrayList<>();
    }

    public Species(Stream<Genome> genomes) {
        this.genomes = genomes.collect(toList());
        this.representative = this.genomes.get(new Random().nextInt(this.genomes.size()));
    }

    public void add(Genome genome) {
        if(isNull(representative)) {
            representative = genome;
        }
        genomes.add(genome);
    }

    public Genome getRepresentative() {
        return representative;
    }

    public Stream<Genome> getGenomes() {
        return genomes.stream();
    }

    public long getSize() {
        return genomes.size();
    }
}
