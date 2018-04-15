package genetics.api.definition;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import genetics.api.IGeneticPlugin;
import genetics.api.alleles.IAllele;
import genetics.api.alleles.IAlleleTemplate;
import genetics.api.gene.IGeneFactory;
import genetics.api.gene.IKaryotype;
import genetics.api.individual.IIndividual;
import genetics.api.organism.IOrganismHandler;
import genetics.api.organism.IOrganismType;
import genetics.api.organism.IOrganismTypes;

/**
 * The IGeneticDefinitionBuilder offers several functions to register templates, types or something similar that can be
 * later retrieved from the IGeneticDefinition.
 * <p>
 * After every {@link IGeneticPlugin} received {@link IGeneticPlugin#register(IGeneFactory)} all
 * {@link IIndividualDefinitionBuilder}s will be build automatically to {@link IIndividualDefinition}s. You can the instance
 * of you definition from {@link IDefinitionRegistry#getDefinition(String)} after it was created.
 * <p>
 * You can create a instance of this with {@link IGeneFactory#createDefinition(String, IKaryotype, Function)}.
 *
 * @param <I> The type of the individual that the definition describes.
 */
public interface IIndividualDefinitionBuilder<I extends IIndividual, R extends IIndividualRoot<I>> {

	IIndividualDefinitionBuilder<I, R> registerType(IOrganismType type, IOrganismHandler<I> handler);

	/**
	 * @param translatorKey The key of the translator the block of {@link IBlockState} that you want to translate
	 *                      with the translator.
	 * @param translator    A translator that should be used to translate the data.
	 */
	IIndividualDefinitionBuilder<I, R> registerTranslator(Block translatorKey, IBlockTranslator<I> translator);

	/**
	 * @param translatorKey The key of the translator it is the item of the {@link ItemStack} that you want to translate
	 *                      with the translator.
	 * @param translator    A translator that should be used to translate the data.
	 */
	IIndividualDefinitionBuilder<I, R> registerTranslator(Item translatorKey, IItemTranslator<I> translator);

	/**
	 * Registers a allele template using the UID of the first allele as identifier.
	 */
	IIndividualDefinitionBuilder<I, R> registerTemplate(IAllele[] template);

	/**
	 * Registers a allele template using the passed identifier.
	 */
	//TODO: REMOVE ?
	IIndividualDefinitionBuilder<I, R> registerTemplate(String identifier, IAllele[] template);

	/**
	 * Registers a allele template using the UID of the first allele as identifier.
	 */
	IIndividualDefinitionBuilder<I, R> registerTemplate(IAlleleTemplate template);

	/**
	 * Registers a allele template using the passed identifier.
	 */
	//TODO: REMOVE ?
	IIndividualDefinitionBuilder<I, R> registerTemplate(String identifier, IAlleleTemplate template);

	IIndividualDefinitionBuilder<I, R> setTranslator(BiFunction<Map<Item, IItemTranslator<I>>, Map<Block, IBlockTranslator<I>>, IIndividualTranslator<I>> translatorFactory);

	IIndividualDefinitionBuilder<I, R> setTypes(Function<Map<IOrganismType, IOrganismHandler<I>>, IOrganismTypes<I>> typesFactory);

	/**
	 * The karyotype that was used to create this builder.
	 */
	IKaryotype getKaryotype();

	IOptionalDefinition<I, R> getOptional();
}
