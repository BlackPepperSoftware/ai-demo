package neuroevolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Population
{
	private List<Species> speciesList;
	
	public Population(int size)
	{
		speciesList = new ArrayList<>();
		
		// TODO: set genome neuron count to inputs
		Stream.generate(Genome::new)
			.map(Genome::mutate)
			.limit(size)
			.forEach(this::addGenome);
	}
	
	private void addGenome(Genome genome)
	{
		findSpecies(genome)
			.orElseGet(this::addSpecies)
			.addGenome(genome);
	}
	
	private Species addSpecies()
	{
		Species species = new Species();
		speciesList.add(species);
		return species;
	}
	
	private Optional<Species> findSpecies(Genome genome)
	{
		return speciesList.stream()
			.filter(species -> species.getGenomes()
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Empty species"))
				.sameSpecies(genome)
			)
			.findFirst();
	}
}
