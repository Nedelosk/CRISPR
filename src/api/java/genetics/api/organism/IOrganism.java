package genetics.api.organism;

import java.util.Optional;

import net.minecraft.item.ItemStack;

import genetics.api.IGeneticFactory;
import genetics.api.alleles.IAllele;
import genetics.api.gene.IChromosomeType;
import genetics.api.individual.IIndividual;
import genetics.api.root.IIndividualRoot;

/**
 * Implement this interface as a capability which should provide the genetic information of an item.
 * <p/>
 * You can use {@link IGeneticFactory#createOrganism(ItemStack, IOrganismType, IIndividualRoot)} to create an
 * instance of this or you can use your own implementation.
 */
public interface IOrganism<I extends IIndividual> {

	/**
	 * @return Creates the individual out of the nbt of the item.
	 */
	Optional<I> getIndividual();

	/**
	 * Writes the given individual to the nbt of this item.
	 */
	boolean setIndividual(I individual);

	/**
	 * @return The root of the individual.
	 */
	IIndividualRoot<I> getRoot();

	/**
	 * @return The species type of the individual.
	 */
	IOrganismType getType();

	/**
	 * Quickly gets the allele without loading the whole genome and creates it if it is absent.
	 *
	 * @param type   The chromosome type of the chromosome that contains the allele.
	 * @param active True if the allele should be the active allele of the chromosome, false if not.
	 * @return The allele that is at that position of the genome.
	 */
	IAllele getAllele(IChromosomeType type, boolean active);

	/**
	 * Quickly gets the allele without loading the whole genome. And without creating absent chromosomes and alleles.
	 *
	 * @param type   The chromosome type of the chromosome that contains the allele.
	 * @param active True if the allele should be the active allele of the chromosome, false if not.
	 * @return The allele that is at that position of the genome.
	 */
	Optional<IAllele> getAlleleDirectly(IChromosomeType type, boolean active);
}