package genetics.api;

import genetics.api.alleles.IAlleleConstant;
import genetics.api.individual.IChromosomeType;

/**
 * The IRegistryHelper offers a functions to create early genes.
 * <p>
 * The only instance of this is passed is passed to your genetic plugin in {@link IGeneticPlugin#registerSimple(IRegistryHelper)}.
 */
public interface IRegistryHelper {
	/**
	 * Creates a allele for every {@link IAlleleConstant} that the given array contains and
	 * creates later a {@link IGeneBuilder} that has the {@link IChromosomeType} of the given type and the given name
	 * of the {@link IAlleleConstant} array.
	 *
	 * @param constants A array that contains the information that is needed to create the alleles that the gene should
	 *                  contain.
	 * @param types     The type of the gene
	 */
	void addAlleles(IAlleleConstant[] constants, IChromosomeType... types);

}
