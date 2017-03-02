package neuroevolution;

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
}
