package genetics;

import net.minecraft.item.ItemStack;

import genetics.api.IGeneticFactory;
import genetics.api.alleles.IAllele;
import genetics.api.alleles.IAlleleTemplateBuilder;
import genetics.api.definition.IGeneticDefinition;
import genetics.api.definition.IGeneticRoot;
import genetics.api.individual.IGeneticType;
import genetics.api.individual.IIndividual;
import genetics.api.individual.IIndividualHandler;

import genetics.alleles.AlleleTemplateBuilder;
import genetics.individual.IndividualHandler;

public enum GeneticFactory implements IGeneticFactory {
	INSTANCE;

	@Override
	public IAlleleTemplateBuilder createTemplate(IGeneticDefinition definition) {
		return new AlleleTemplateBuilder(definition, definition.getDefaultTemplate().alleles());
	}

	@Override
	public IAlleleTemplateBuilder createTemplate(IGeneticDefinition definition, IAllele[] alleles) {
		return new AlleleTemplateBuilder(definition, alleles);
	}

	@Override
	public <I extends IIndividual> IIndividualHandler<I> createIndividualHandler(ItemStack itemStack, IGeneticType type, IGeneticDefinition<I, IGeneticRoot> definition) {
		return new IndividualHandler<>(itemStack, () -> definition, () -> type);
	}
}
