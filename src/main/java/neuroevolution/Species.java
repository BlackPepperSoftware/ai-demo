package neuroevolution;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Species
{
	private List<Genome> genomes;
	
	public Species()
	{
		genomes = new ArrayList<>();
	}
	
	public void addGenome(Genome genome)
	{
		genomes.add(genome);
	}
	
	public Stream<Genome> getGenomes()
	{
		return genomes.stream();
	}
	
	public boolean isCompatibleWith(Genome genome)
	{
		Genome someGenome = getGenomes().findAny()
			.orElseThrow(() -> new IllegalStateException("Empty species"));
		
		return new GenomeComparator().compare(someGenome, genome) == 0;
	}
	
	public void print(PrintStream out)
	{
		out.println("Species:");
		
		genomes.stream()
			.forEach(genome -> genome.print(out));
	}
}
