package neuroevolution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Genome
{
	private final List<Gene> genes;
	
	public Genome(Gene... genes)
	{
		this.genes = new ArrayList<>(asList(genes));
	}
	
	public Genome(Genome that)
	{
		genes = that.getGenes()
			.map(Gene::new)
			.collect(toList());
	}
	
	public Stream<Gene> getGenes()
	{
		return genes.stream();
	}
	
	public Genome mutate()
	{
		Genome genome = new Genome(this);
		
		// TODO: mutate
		
		return genome;
	}
}
