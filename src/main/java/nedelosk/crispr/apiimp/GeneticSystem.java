package nedelosk.crispr.apiimp;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import nedelosk.crispr.api.IGeneticDefinition;
import nedelosk.crispr.api.IGeneticRoot;
import nedelosk.crispr.api.IGeneticSystem;
import nedelosk.crispr.api.gene.IGene;
import nedelosk.crispr.api.gene.IGeneType;
import nedelosk.crispr.api.individual.IIndividual;

public class GeneticSystem implements IGeneticSystem {
	private final HashMap<IGeneType, IGene> geneByType = new HashMap<>();
	private final Multimap<IGene, IGeneType> typesByGene = HashMultimap.create();
	private final HashMap<String, IGeneticDefinition> definitions = new HashMap<>();

	@Override
	public Collection<IGeneticDefinition> getDefinitions() {
		return definitions.values();
	}

	@Override
	public void registerDefinition(IGeneticDefinition definition) {
		definitions.put(definition.getName(), definition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <I extends IIndividual, R extends IGeneticRoot<I, ?>> Optional<IGeneticDefinition<I, R>> getDefinition(String name) {
		return Optional.ofNullable((IGeneticDefinition<I, R>) definitions.get(name));
	}

	@Override
	public void registerGene(IGene gene, IGeneType... types) {
		Arrays.stream(types).forEach(k -> {
			geneByType.put(k, gene);
			typesByGene.put(gene, k);
		});
	}

	@Override
	public Optional<IGene> getGene(IGeneType type) {
		return Optional.ofNullable(geneByType.get(type));
	}

	@Override
	public Collection<IGeneType> getTypes() {
		return geneByType.keySet();
	}

	@Override
	public Collection<IGeneType> getTypes(IGene gene) {
		return typesByGene.get(gene);
	}
}
